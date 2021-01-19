#include <cstdint>
#include <map>
#ifdef __CYGWIN__
#define __int64 int64_t
#endif
#include <string.h>
#include "Context.h"
#include "tonclient.h"

JavaVM *javaVM;

struct call_site {
    jobject target;
    jobject promise;
};

std::map<uint32_t, jobject> application_map;

tc_string_data_t tc_string(const char *string) {
    return tc_string_data_t{string, (uint32_t) strlen(string)};
}

jstring jni_string(JNIEnv *env, tc_string_data_t string) {
    char *pChar = new char[string.len + 1];
    strncpy(pChar, string.content, string.len);
    pChar[string.len] = 0;
    jstring result = env->NewStringUTF(pChar);
    delete[] pChar;
    return result;
}

void response_handler_ptr(void* request_ptr, tc_string_data_t params_json, uint32_t response_type, bool finished) {
    JNIEnv *env;
    bool needDetach = false;
    int envStat = javaVM->GetEnv((void **) &env, JNI_VERSION_1_8);
    if (envStat == JNI_EDETACHED) {
        javaVM->AttachCurrentThread((void **) &env, nullptr);
        needDetach = true;
    }

    auto* site = (call_site*)request_ptr;

    jclass clazz = env->GetObjectClass(site -> target);
    jmethodID jHandler = env->GetMethodID(clazz, "asyncHandler", "(ILjava/lang/String;Lscala/concurrent/Promise;Z)V");
    env->CallVoidMethod(site -> target, jHandler, response_type, jni_string(env, params_json), site -> promise, finished);

    if (needDetach) {
        javaVM->DetachCurrentThread();
    }
    if (finished) {
        delete site;
    }
}

void application_handler(uint32_t request_id, tc_string_data_t params_json, uint32_t response_type, bool finished) {
    JNIEnv *env;
    bool needDetach = false;
    int envStat = javaVM->GetEnv((void **) &env, JNI_VERSION_1_8);
    if (envStat == JNI_EDETACHED) {
        javaVM->AttachCurrentThread((void **) &env, nullptr);
        needDetach = true;
    }

    jobject targetObj = application_map[request_id];

    jclass clazz = env->GetObjectClass(targetObj);
    jmethodID jHandler = env->GetMethodID(clazz, "asyncHandlerWithAppId", "(ILjava/lang/String;IZ)V");
    env->CallVoidMethod(targetObj, jHandler, response_type, jni_string(env, params_json), request_id, finished);

    if (needDetach) {
        javaVM->DetachCurrentThread();
    }
}

void unregister_handler(uint32_t request_id, tc_string_data_t params_json, uint32_t response_type, bool finished) {
    JNIEnv *env;
    bool needDetach = false;
    int envStat = javaVM->GetEnv((void **) &env, JNI_VERSION_1_8);
    if (envStat == JNI_EDETACHED) {
        javaVM->AttachCurrentThread((void **) &env, nullptr);
        needDetach = true;
    }

    jobject targetObj = application_map[request_id];

    jclass clazz = env->GetObjectClass(targetObj);
    jmethodID jHandler = env->GetMethodID(clazz, "asyncHandlerWithAppId", "(ILjava/lang/String;IZ)V");
    env->CallVoidMethod(targetObj, jHandler, response_type, jni_string(env, params_json), request_id, finished);

    application_map.erase(request_id);

    if (needDetach) {
        javaVM->DetachCurrentThread();
    }
}

JNIEXPORT jstring JNICALL
Java_com_radiance_jvm_Context_createContext(JNIEnv *env, jobject obj, jstring jConfig) {
    env->
            GetJavaVM(&javaVM);
    const char *config = env->GetStringUTFChars(jConfig, nullptr);
    tc_string_handle_t *handle = tc_create_context(tc_string(config));
    tc_string_data_t context = tc_read_string(handle);
    env->
            ReleaseStringUTFChars(jConfig, config);
    jstring result = jni_string(env, context);
    tc_destroy_string(handle);
    return result;
}

JNIEXPORT void JNICALL
Java_com_radiance_jvm_Context_destroyContext(
        JNIEnv *env,
        jobject obj,
        jint ctx
) {
    tc_destroy_context((uint32_t) ctx);
}

JNIEXPORT void JNICALL
Java_com_radiance_jvm_Context_asyncRequest(
        JNIEnv *env,
        jobject jobj,
        jint ctx,
        jstring jName,
        jstring jParams,
        jobject jpromise
) {
    const char *name = env->GetStringUTFChars(jName, nullptr);
    const char *params = env->GetStringUTFChars(jParams, nullptr);

    jobject jobjGlobal = env->NewGlobalRef(jobj);
    jobject jpromiseGlobal = env->NewGlobalRef(jpromise);
    auto* site = new call_site{jobjGlobal, jpromiseGlobal};

    tc_response_handler_ptr_t callback = &response_handler_ptr;
    tc_request_ptr((uint32_t)ctx, tc_string(name), tc_string(params), (void*) site, callback);
    env-> ReleaseStringUTFChars(jName, name);
    env-> ReleaseStringUTFChars(jParams, params);
}

JNIEXPORT void JNICALL
Java_com_radiance_jvm_Context_asyncRequestWithAppId(
        JNIEnv *env,
        jobject jobj,
        jint ctx,
        jstring jName,
        jstring jParams,
        jint appId
) {
    const char *name = env->GetStringUTFChars(jName, nullptr);
    const char *params = env->GetStringUTFChars(jParams, nullptr);

    jobject jobjGlobal = env->NewGlobalRef(jobj);
    application_map[appId] = jobjGlobal;
    tc_response_handler_t callback = &application_handler;
    tc_request((uint32_t)ctx, tc_string(name), tc_string(params), appId, callback);
    env-> ReleaseStringUTFChars(jName, name);
    env-> ReleaseStringUTFChars(jParams, params);
}

JNIEXPORT void JNICALL
Java_com_radiance_jvm_Context_unregisterAppId(
        JNIEnv *env,
        jobject jobj,
        jint ctx,
        jstring jName,
        jstring jParams,
        jint appId
) {
    const char *name = env->GetStringUTFChars(jName, nullptr);
    const char *params = env->GetStringUTFChars(jParams, nullptr);

    jobject jobjGlobal = env->NewGlobalRef(jobj);
    application_map[appId] = jobjGlobal;
    tc_response_handler_t callback = &unregister_handler;
    tc_request((uint32_t)ctx, tc_string(name), tc_string(params), appId, callback);
    env-> ReleaseStringUTFChars(jName, name);
    env-> ReleaseStringUTFChars(jParams, params);
}

JNIEXPORT jstring JNICALL
Java_com_radiance_jvm_Context_syncRequest(
        JNIEnv *env,
        jobject obj,
        jint ctx,
        jstring jName,
        jstring jParams
) {
    const char *name = env->GetStringUTFChars(jName, nullptr);
    const char *params = env->GetStringUTFChars(jParams, nullptr);
    const tc_string_handle_t *handle = tc_request_sync((uint32_t) ctx, tc_string(name), tc_string(params));
    tc_string_data_t response = tc_read_string(handle);
    jstring res = jni_string(env, response);
    tc_destroy_string(handle);
    env-> ReleaseStringUTFChars(jName, name);
    env-> ReleaseStringUTFChars(jParams, params);

    return res;
}