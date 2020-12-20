/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_radiance_jvm_Context */

#ifndef _Included_com_radiance_jvm_Context
#define _Included_com_radiance_jvm_Context
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:      com_radiance_jvm_Context
 * Method:     createContext
 * Signature:  (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_radiance_jvm_Context_createContext
        (JNIEnv*, jobject, jstring);

/*
 * Class:      com_radiance_jvm_Context
 * Method:     destroyContext
 * Signature:  (I)V
 */
JNIEXPORT void JNICALL Java_com_radiance_jvm_Context_destroyContext
(JNIEnv*, jobject, jint);

/*
 * Class:      com_radiance_jvm_Context
 * Method:     asyncRequest
 * Signature:  (ILjava/lang/String;Ljava/lang/String;Lscala/concurrent/Promise;)V
 */
JNIEXPORT void JNICALL Java_com_radiance_jvm_Context_asyncRequest
(JNIEnv*, jobject, jint, jstring, jstring, jobject);

/*
 * Class:      com_radiance_jvm_Context
 * Method:     syncRequest
 * Signature:  (ILjava/lang/String;Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_radiance_jvm_Context_syncRequest
        (JNIEnv*, jobject, jint, jstring, jstring);

#ifdef __cplusplus
}
#endif
#endif