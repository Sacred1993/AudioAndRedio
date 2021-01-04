package me.xyp.audioandvedio.audio

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Message
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.Executors

/**
 * Author: xuyanpeng
 * Date: 2020/12/30 10:27
 * Description:
 */
class AudioRecordManager private constructor() {
    private val TAG = AudioRecordManager::class.java.simpleName

    companion object {
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = AudioRecordManager()
    }

    private var audioRecord: AudioRecord? = null

    //默认采样率
    private val DEFAULT_SAMPLING_RATE: Int = 44100//采样频率

    //转换周期，录音每满160帧，进行一次转换
    private val FRAME_COUNT = 160

    //输出MP3的码率
    private val BIT_RATE = 32

    //根据资料假定的最大值。 实测时有时超过此值。
    private val MAX_VOLUME = 2000

    private val channelConfig =
        AudioFormat.CHANNEL_IN_MONO//单声道，立体声道：AudioFormat.CHANNEL_IN_STEREO

    private val audioFormat = PCMFormat.PCM_16BIT//采样格式

    private var bufferSize = 0 //缓存区大小

    private var mPCMBuffer: ShortArray? = null

    private var encodeThread: DataEncodeThread? = null
    private val executor = Executors.newFixedThreadPool(1)
    private var fos: FileOutputStream? = null
    private var isRecording = false
    private var mVolume = 0
    private var sampleRate = DEFAULT_SAMPLING_RATE

    private val finishListener: OnFinishListener? = null


    init {
        val bytesPerFrame = audioFormat.bytesPerFrame
        var frameSize = AudioRecord.getMinBufferSize(
            sampleRate,
            channelConfig,
            audioFormat.audioFormat
        ) / bytesPerFrame
        /*调整缓存区帧数大小为FRAME_COUNT的倍数*/
        if (frameSize % FRAME_COUNT != 0) {
            frameSize += (FRAME_COUNT - frameSize % FRAME_COUNT)
            Log.d(TAG, "Frame size: $frameSize")
        }
        /*缓存区字节数大小*/
        bufferSize = frameSize * bytesPerFrame
        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,//输入源：麦克风
            DEFAULT_SAMPLING_RATE,
            channelConfig,
            audioFormat.audioFormat,
            bufferSize
        )
        mPCMBuffer = ShortArray(bufferSize)
        SimpleLame.init(sampleRate, 1, sampleRate, BIT_RATE)
    }


    fun startRecord(mp3File: File) {
        if (audioRecord!!.state != AudioRecord.STATE_INITIALIZED) {
            Log.e(TAG, "未获得录音权限")
            return
        }
        if (isRecording) return

        fos = FileOutputStream(mp3File)
        // Create and run thread used to encode data
        encodeThread = DataEncodeThread(fos, bufferSize)
        encodeThread!!.start()

        //给AudioRecord设置刷新监听，待录音帧数每次达到FRAME_COUNT，就通知转换线程转换一次数据
        audioRecord!!.setRecordPositionUpdateListener(encodeThread, encodeThread!!.handler)
        audioRecord!!.positionNotificationPeriod = FRAME_COUNT

        audioRecord!!.startRecording()

        val runnable = Runnable {
            isRecording = true
            //循环的从AudioRecord获取录音的PCM数据
            while (isRecording) {
                val readSize = audioRecord!!.read(mPCMBuffer!!, 0, bufferSize)
                if (readSize > 0) {
                    //待转换的PCM数据放到转换线程中
                    encodeThread!!.addChangeBuffer(mPCMBuffer, readSize)
                    calculateRealVolume(mPCMBuffer!!, readSize)
                }
            }

            // 录音完毕，释放AudioRecord的资源
            try {
                audioRecord!!.stop()
                audioRecord!!.release()
                audioRecord = null

                // 录音完毕，通知转换线程停止，并等待直到其转换完毕
                val msg: Message =
                    Message.obtain(encodeThread!!.handler, DataEncodeThread.PROCESS_STOP)
                msg.sendToTarget()
                Log.d(TAG, "waiting for encoding thread")
                encodeThread!!.join()
                Log.d(TAG, "done encoding thread")
                //转换完毕后回调监听
                finishListener?.onFinish(mp3File.path)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } finally {
                try {
                    fos?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        executor.execute(runnable)
    }

    fun stopRecording() {
        Log.d(TAG, "stop recording")
        isRecording = false
    }

    //计算音量大小
    private fun calculateRealVolume(buffer: ShortArray, readSize: Int) {
        var sum = 0.0
        for (i in 0 until readSize) {
            sum += (buffer[i] * buffer[i]).toDouble()
        }
        if (readSize > 0) {
            val amplitude = sum / readSize
            mVolume = Math.sqrt(amplitude).toInt()
        }
    }

    fun getVolume(): Int {
        return if (mVolume >= MAX_VOLUME) {
            MAX_VOLUME
        } else mVolume
    }


    interface OnFinishListener {
        fun onFinish(mp3SavePath: String?)
    }
}