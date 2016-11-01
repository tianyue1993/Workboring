/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_etcomm_dcare_Watcher */
//com_etcomm_dcare_Watcher
#ifndef _Included_com_etcomm_dcare_Watcher
#define _Included_com_etcomm_dcare_Watcher
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_etcomm_dcare_Watcher
 * Method:    createWatcher
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_etcomm_dcare_Watcher_createWatcher
  (JNIEnv *, jobject, jstring);

/*
 * Class:     com_etcomm_dcare_Watcher
 * Method:    connectToMonitor
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_etcomm_dcare_Watcher_connectToMonitor
  (JNIEnv *, jobject);

/*
 * Class:     com_etcomm_dcare_Watcher
 * Method:    sendMsgToMonitor
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_etcomm_dcare_Watcher_sendMsgToMonitor
  (JNIEnv *, jobject, jstring);
/*
 * Class:     com_etcomm_dcare_Watcher
 * Method:    nativegetCurSteps
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_etcomm_dcare_Watcher_nativegetCurSteps
  (JNIEnv *, jobject);
/*
 * Class:     com_etcomm_dcare_Watcher
 * Method:    nativeresetCalMileData
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_etcomm_dcare_Watcher_nativeresetCalMileData
  (JNIEnv *, jobject);
/*
 * Class:     com_etcomm_dcare_Watcher
 * Method:    setnativeSensitivity
 * Signature:
 */
JNIEXPORT jint JNICALL Java_com_etcomm_dcare_Watcher_setnativeSensitivity
  (JNIEnv *, jobject, jfloat);
/*
 * Class:     com_etcomm_dcare_Watcher
 * Method:    setEnablePedometer
 * Signature:
 */
JNIEXPORT jint JNICALL Java_com_etcomm_dcare_Watcher_setEnablePedometer
(JNIEnv *, jobject, jboolean);
/*
 * Class:     com_etcomm_dcare_Watcher
 * Method:    setnativefilepath
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_etcomm_dcare_Watcher_setnativeSensitivity
  (JNIEnv *, jobject, jfloat);
/*
 * Class:     com_etcomm_dcare_Watcher
 * Method:    setnativefilepath
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_etcomm_dcare_Watcher_setnativefilepath
  (JNIEnv *, jobject, jstring);


/*
 * Class:     com_etcomm_dcare_Watcher
 * Method:    startSensorManager
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_etcomm_dcare_Watcher_startSensorManager
  (JNIEnv *, jobject);

/*
 * Class:     com_etcomm_dcare_Watcher
 * Method:    restartSensorManager
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_etcomm_dcare_Watcher_restartSensorManager
  (JNIEnv *, jobject);

JNIEXPORT jfloatArray JNICALL Java_com_etcomm_dcare_Watcher_timerIncreased
  (JNIEnv *, jobject);

JNIEXPORT jint JNICALL Java_com_etcomm_dcare_Watcher_nativeSetWeightHeightSteplenth
  (JNIEnv *, jobject,jfloat,jfloat,jfloat);

#ifdef __cplusplus
}
#endif
#endif
