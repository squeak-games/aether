#include <oboe/Oboe.h>
#include <jni.h>
#include <cmath>
#include "RingBuffer.h"

static constexpr int kRingBufferCapacity = 64;
static RingBuffer* gCommandBuffer = new RingBuffer(kRingBufferCapacity);

class SineOscillator : public oboe::AudioStreamDataCallback {
public:
    explicit SineOscillator() : stream_(nullptr), frequency_(440.0f), phase_(0.0) {}

    oboe::Result openStream() {
        oboe::AudioStreamBuilder builder;
        builder.setDirection(oboe::Direction::Output);
        builder.setPerformanceMode(oboe::PerformanceMode::LowLatency);
        builder.setSharingMode(oboe::SharingMode::Exclusive);
        builder.setFormat(oboe::AudioFormat::Float);
        builder.setChannelCount(1);
        builder.setSampleRate(48000);
        builder.setDataCallback(this);
        return builder.openStream(&stream_);
    }

    oboe::Result start() {
        if (!stream_) return oboe::Result::ErrorInvalidState;
        return stream_->start();
    }

    oboe::Result stop() {
        if (!stream_) return oboe::Result::ErrorInvalidState;
        return stream_->stop();
    }

    oboe::Result closeStream() {
        if (!stream_) return oboe::Result::ErrorInvalidState;
        return stream_->close();
    }

    void setFrequency(float hz) { frequency_ = hz; }
    float frequency() const { return frequency_; }

    oboe::DataCallbackResult onAudioReady(
        oboe::AudioStream* /* stream */,
        void* audioData,
        int32_t numFrames
    ) override {
        auto* floatData = static_cast<float*>(audioData);
        const double sampleRate = 48000.0;
        for (int32_t i = 0; i < numFrames; ++i) {
            floatData[i] = static_cast<float>(std::sin(phase_));
            phase_ += 2.0 * M_PI * frequency_ / sampleRate;
            if (phase_ > 2.0 * M_PI) phase_ -= 2.0 * M_PI;
        }
        return oboe::DataCallbackResult::Continue;
    }

private:
    oboe::AudioStream* stream_;
    float frequency_;
    double phase_;
};

static SineOscillator* gOscillator = nullptr;

extern "C" JNIEXPORT jboolean JNICALL
Java_com_squeakgames_aether_audio_AetherAudioEngine_nativeStart(
    JNIEnv* /* env */,
    jobject /* thiz */) {
    if (gOscillator) return JNI_FALSE;
    gOscillator = new SineOscillator();
    if (gOscillator->openStream() != oboe::Result::OK) {
        delete gOscillator;
        gOscillator = nullptr;
        return JNI_FALSE;
    }
    return gOscillator->start() == oboe::Result::OK ? JNI_TRUE : JNI_FALSE;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_squeakgames_aether_audio_AetherAudioEngine_nativeStop(
    JNIEnv* /* env */,
    jobject /* thiz */) {
    if (!gOscillator) return JNI_FALSE;
    auto result = gOscillator->stop();
    gOscillator->closeStream();
    delete gOscillator;
    gOscillator = nullptr;
    return result == oboe::Result::OK ? JNI_TRUE : JNI_FALSE;
}

extern "C" JNIEXPORT void JNICALL
Java_com_squeakgames_aether_audio_AetherAudioEngine_nativeSetFrequency(
    JNIEnv* /* env */,
    jobject /* thiz */,
    jfloat hz) {
    if (gOscillator) {
        gOscillator->setFrequency(hz);
    }
}

extern "C" JNIEXPORT jfloat JNICALL
Java_com_squeakgames_aether_audio_AetherAudioEngine_nativeGetFrequency(
    JNIEnv* /* env */,
    jobject /* thiz */) {
    return gOscillator ? gOscillator->frequency() : 0.0f;
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
