package me.xyp.audioandvedio

import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import me.xyp.audioandvedio.audio.AudioRecordManager
import me.xyp.audioandvedio.audio.SimpleLame
import java.io.File

class MainActivity : AppCompatActivity() {
    private val filePath = getBasePath()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        start_record_btn.setOnClickListener {
            AudioRecordManager.instance.startRecord(createMp3SaveFile(filePath + File.separator + "myLame.mp3"))
        }
        stop_record_btn.setOnClickListener {
            AudioRecordManager.instance.stopRecording()
        }
        testffmpeg()
    }

    private fun createMp3SaveFile(saveMp3FullName: String): File {
        val mp3 = File(saveMp3FullName)
        return mp3
    }

    private fun getBasePath(): String{
        var strPath: String = ""
        strPath = if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            getFilesDir().toString() + "/" + "lameMp3"
        } else {
            Environment.getExternalStorageDirectory().toString() + "/" + "lameMp3"
        }
        val dir = File(strPath)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return strPath
    }


    private fun test() {
//        SimpleLame.init(44100, 1, 44100, 32)
//        val jniTest = JniTest()
//        println(jniTest.get())
//        textView.text = jniTest.get() + ">>>" + stringFromJNI() + ">>>"
    }

    private fun testffmpeg(){
        textView.text ="hello"+ffmpegInfo()
    }

//    external fun stringFromJNI(): String
    private external fun ffmpegInfo(): String
    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
//            System.loadLibrary("jni-test")
//            System.loadLibrary("lamemp3")
        }
    }
}