/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class me_xyp_audioandvedio_audio_SimpleLame */

#ifndef _Included_me_xyp_audioandvedio_audio_SimpleLame
#define _Included_me_xyp_audioandvedio_audio_SimpleLame
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     me_xyp_audioandvedio_audio_SimpleLame
 * Method:    close
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_me_xyp_audioandvedio_audio_SimpleLame_close
  (JNIEnv *, jclass);

/*
 * Class:     me_xyp_audioandvedio_audio_SimpleLame
 * Method:    encode
 * Signature: ([S[SI[B)I
 */
JNIEXPORT jint JNICALL Java_me_xyp_audioandvedio_audio_SimpleLame_encode
  (JNIEnv *, jclass, jshortArray, jshortArray, jint, jbyteArray);

/*
 * Class:     me_xyp_audioandvedio_audio_SimpleLame
 * Method:    flush
 * Signature: ([B)I
 */
JNIEXPORT jint JNICALL Java_me_xyp_audioandvedio_audio_SimpleLame_flush
  (JNIEnv *, jclass, jbyteArray);

/*
 * Class:     me_xyp_audioandvedio_audio_SimpleLame
 * Method:    init
 * Signature: (IIIII)V
 */
JNIEXPORT void JNICALL Java_me_xyp_audioandvedio_audio_SimpleLame_init
  (JNIEnv *, jclass, jint, jint, jint, jint, jint);

#ifdef __cplusplus
}
#endif
#endif
