// Aether audio engine — native Oboe synthesis bridge.
// Stub scaffold; Oboe integration and oscillator bank land in v0.0.2.

#include <jni.h>

extern "C" JNIEXPORT jstring JNICALL
Java_com_squeakgames_aether_audio_AetherAudioEngine_nativeGetVersion(
    JNIEnv* env,
    jobject /* this */) {
    return env->NewStringUTF("0.0.1-scaffold");
}
