#include <stdint.h>
#include <string.h>
#include "TonContextScala.h"
#include "tonclient.h"
#include <map>
#include <mutex>

JavaVM *javaVM;

struct call_site {
    jobject target;
    jobject promise;
};

std::mutex call_site_lock;
std::map<uint32_t, call_site> call_site_storage;

tc_string_data_t tc_string(const char *string) {
    return tc_string_data_t{string, (uint32_t) strlen(string)};
}

jstring jni_string(JNIEnv *env, tc_string_data_t
string) {
    char *pChar = new char[string.len + 1];
    strncpy_s(pChar, (size_t)
                             string.len + 1, string.content, string.len);
    pChar[string.len] = 0;
    jstring result = env->NewStringUTF(pChar);
    delete[]
            pChar;
    return
            result;
}

void response_handler(uint32_t request_id, tc_string_data_t json, uint32_t response_type, bool finished) {
    printf("response %d %d\n", request_id, response_type);
    JNIEnv *env;
    bool needDetach = false;
    int envStat = javaVM->GetEnv((void **) &env, JNI_VERSION_1_8);
    if (envStat == JNI_EDETACHED) {
        javaVM->AttachCurrentThread((void **) &env, NULL);
        needDetach = true;
    }
    call_site_lock.lock();
    call_site site = call_site_storage[request_id];
    call_site_storage.erase(request_id);
    call_site_lock.unlock();
    jclass clazz = env->GetObjectClass(site.target);
    jmethodID jHandler = env->GetMethodID(clazz, "asyncHandler", "(ILjava/lang/String;Lscala/concurrent/Promise;)V");
    env->CallVoidMethod(site.target, jHandler, response_type, jni_string(env, json), site.promise);

    if (needDetach) {
        javaVM->DetachCurrentThread();
    }
}

JNIEXPORT jstring
JNICALL Java_com_radiance_scala_tonclient_TonContextScala_createContext(JNIEnv *env, jobject
obj,
                                                                        jstring jConfig
) {
    env->
            GetJavaVM(&javaVM);
    const char *config = env->GetStringUTFChars(jConfig, NULL);
    tc_string_handle_t *handle = tc_create_context(tc_string(config));
    tc_string_data_t context = tc_read_string(handle);
    env->
            ReleaseStringUTFChars(jConfig, config
    );
    jstring result = jni_string(env, context);
    tc_destroy_string(handle);
    return
            result;
}

JNIEXPORT void JNICALL
Java_com_radiance_scala_tonclient_TonContextScala_destroyContext(JNIEnv
                                                                 *env,
                                                                 jobject obj, jint
                                                                 ctx) {
    tc_destroy_context((uint32_t)
                               ctx);
}

JNIEXPORT void JNICALL
Java_com_radiance_scala_tonclient_TonContextScala_asyncRequest(JNIEnv
                                                               *env,
                                                               jobject jobj, jint
                                                               ctx,
                                                               jstring jName, jstring
                                                               jParams,
                                                               jobject jpromise
) {
    const char *name = env->GetStringUTFChars(jName, NULL);
    const char *params = env->GetStringUTFChars(jParams, NULL);
    tc_string_data_t tcName = tc_string_data_t{name, (uint32_t) strlen(name)};
    // TODO fix it
    uint32_t id = static_cast<uint32_t>(reinterpret_cast<std::uintptr_t>(jpromise));
    jobject jobjGlobal = env->NewGlobalRef(jobj);
    jobject jpromiseGlobal = env->NewGlobalRef(jpromise);
    call_site site = {jobjGlobal, jpromiseGlobal};
    call_site_lock.

            lock();

    call_site_storage[id] =
            site;
    call_site_lock.

            unlock();

    tc_request((uint32_t)
                       ctx,
               tc_string(name), tc_string(params), id, response_handler
    );
    env->
            ReleaseStringUTFChars(jName, name
    );
    env->
            ReleaseStringUTFChars(jParams, params
    );
}

JNIEXPORT jstring
JNICALL Java_com_radiance_scala_tonclient_TonContextScala_syncRequest(JNIEnv *env, jobject
obj,
                                                                      jint ctx, jstring
                                                                      jName,
                                                                      jstring jParams
) {
    const char *name = env->GetStringUTFChars(jName, NULL);
    const char *params = env->GetStringUTFChars(jParams, NULL);
    const tc_string_handle_t *handle = tc_request_sync((uint32_t) ctx, tc_string(name), tc_string(params));
    tc_string_data_t response = tc_read_string(handle);
    jstring res = jni_string(env, response);
    tc_destroy_string(handle);
    env->
            ReleaseStringUTFChars(jName, name
    );
    env->
            ReleaseStringUTFChars(jParams, params
    );
    return
            res;
}



