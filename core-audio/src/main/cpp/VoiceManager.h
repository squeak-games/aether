#ifndef AETHER_VOICE_MANAGER_H
#define AETHER_VOICE_MANAGER_H

#include "AdsrEnvelope.h"
#include "BiquadFilter.h"
#include "SpatialPanner.h"
#include <cstdint>

struct VoiceParams {
    float frequency = 440.0f;
    float amplitude = 0.5f;
    float pan = 0.0f;
    float sourceX = 0.0f, sourceY = 0.0f, sourceZ = 0.0f;
};

class Voice {
public:
    explicit Voice(int id);

    void setParams(const VoiceParams& params);
    void noteOn();
    void noteOff();
    float process(int32_t sampleRate);
    bool isActive() const;

    int id() const { return id_; }
    AdsrEnvelope& envelope() { return envelope_; }

private:
    int id_;
    VoiceParams params_;
    double phase_ = 0.0;
    AdsrEnvelope envelope_;
};

class VoiceManager {
public:
    static constexpr int kMaxVoices = 8;

    VoiceManager();

    int addVoice(const VoiceParams& params);
    bool removeVoice(int voiceId);
    void noteOn(int voiceId);
    void noteOff(int voiceId);
    void setVoiceParams(int voiceId, const VoiceParams& params);
    int activeVoiceCount() const;

    void setListenerPosition(float x, float y, float z, float yaw, float pitch);
    void setMasterFilter(BiquadFilter::Type type, float cutoff, float q);

    float process(int32_t sampleRate);

private:
    Voice* voices_[kMaxVoices];
    int nextId_ = 0;
    int voiceCount_ = 0;
    SpatialPanner panner_;
    BiquadFilter masterFilter_;
    BiquadFilter::Type filterType_ = BiquadFilter::Type::LowPass;
    float filterCutoff_ = 20000.0f;
    float filterQ_ = 0.707f;
};

#endif
