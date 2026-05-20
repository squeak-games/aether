#include <oboe/Oboe.h>
#include <jni.h>
#include <cmath>
#include "RingBuffer.h"
#include "VoiceManager.h"

static constexpr int kRingBufferCapacity = 64;
static RingBuffer* gCommandBuffer = new RingBuffer(kRingBufferCapacity);
static VoiceManager* gVoiceManager = nullptr;
static oboe::AudioStream* gStream = nullptr;
static int32_t gSampleRate = 48000;

class AudioCallback : public oboe::AudioStreamDataCallback {
public:
    oboe::DataCallbackResult onAudioReady(
        oboe::AudioStream* stream,
        void* audioData,
        int32_t numFrames
    ) override {
        auto* floatData = static_cast<float*>(audioData);

        if (!gVoiceManager) {
            for (int32_t i = 0; i < numFrames; ++i) {
                floatData[i] = 0.0f;
            }
            return oboe::DataCallbackResult::Continue;
        }

        for (int32_t i = 0; i < numFrames; ++i) {
            floatData[i] = gVoiceManager->process(stream->getSampleRate());
        }

        return oboe::DataCallbackResult::Continue;
    }
};

static AudioCallback* gAudioCallback = nullptr;

extern "C" JNIEXPORT jboolean JNICALL
Java_com_squeakgames_aether_audio_AetherAudioEngine_nativeStart(
    JNIEnv* /* env */,
    jobject /* thiz */) {
    if (gStream) return JNI_FALSE;

    if (!gVoiceManager) {
        gVoiceManager = new VoiceManager();
    }
    if (!gAudioCallback) {
        gAudioCallback = new AudioCallback();
    }

    oboe::AudioStreamBuilder builder;
    builder.setDirection(oboe::Direction::Output);
    builder.setPerformanceMode(oboe::PerformanceMode::LowLatency);
    builder.setSharingMode(oboe::SharingMode::Exclusive);
    builder.setFormat(oboe::AudioFormat::Float);
    builder.setChannelCount(1);
    builder.setSampleRate(gSampleRate);
    builder.setDataCallback(gAudioCallback);

    oboe::Result result = builder.openStream(&gStream);
    if (result != oboe::Result::OK) {
        gStream = nullptr;
        return JNI_FALSE;
    }

    VoiceParams params;
    params.frequency = 440.0f;
    params.amplitude = 0.5f;
    gVoiceManager->addVoice(params);

    return gStream->start() == oboe::Result::OK ? JNI_TRUE : JNI_FALSE;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_squeakgames_aether_audio_AetherAudioEngine_nativeStop(
    JNIEnv* /* env */,
    jobject /* thiz */) {
    if (!gStream) return JNI_FALSE;

    auto result = gStream->stop();
    gStream->close();
    gStream = nullptr;

    delete gAudioCallback;
    gAudioCallback = nullptr;

    return result == oboe::Result::OK ? JNI_TRUE : JNI_FALSE;
}

extern "C" JNIEXPORT void JNICALL
Java_com_squeakgames_aether_audio_AetherAudioEngine_nativeSetFrequency(
    JNIEnv* /* env */,
    jobject /* thiz */,
    jfloat hz) {
    if (gVoiceManager) {
        // Update first active voice frequency
        VoiceParams params;
        params.frequency = hz;
        params.amplitude = 0.5f;
        gVoiceManager->setVoiceParams(0, params);
    }
}

extern "C" JNIEXPORT jfloat JNICALL
Java_com_squeakgames_aether_audio_AetherAudioEngine_nativeGetFrequency(
    JNIEnv* /* env */,
    jobject /* thiz */) {
    return 440.0f;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_squeakgames_aether_audio_AetherAudioEngine_nativeGetVersion(
    JNIEnv* env,
    jobject /* thiz */) {
    return env->NewStringUTF("0.0.2-audio-prototype");
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_squeakgames_aether_audio_AetherAudioEngine_nativePushCommand(
    JNIEnv* /* env */,
    jobject /* thiz */,
    jint command,
    jfloat value) {
    RingBufferEntry entry{command, value};
    return gCommandBuffer->push(entry) ? JNI_TRUE : JNI_FALSE;
}

extern "C" JNIEXPORT jfloatArray JNICALL
Java_com_squeakgames_aether_audio_AetherAudioEngine_nativePopCommand(
    JNIEnv* env,
    jobject /* thiz */) {
    RingBufferEntry entry;
    if (!gCommandBuffer->pop(entry)) {
        return nullptr;
    }
    jfloatArray result = env->NewFloatArray(2);
    jfloat values[2] = {static_cast<jfloat>(entry.command), entry.value};
    env->SetFloatArrayRegion(result, 0, 2, values);
    return result;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_squeakgames_aether_audio_AetherAudioEngine_nativeRingBufferSize(
    JNIEnv* /* env */,
    jobject /* thiz */) {
    return static_cast<jint>(gCommandBuffer->size());
}
