#include "VoiceManager.h"
#include <cmath>
#include <algorithm>

Voice::Voice(int id) : id_(id) {}

void Voice::setParams(const VoiceParams& params) {
    params_ = params;
}

void Voice::noteOn() {
    envelope_.noteOn();
}

void Voice::noteOff() {
    envelope_.noteOff();
}

float Voice::process(int32_t sampleRate) {
    if (envelope_.state() == AdsrEnvelope::State::Idle && envelope_.isActive() == false) {
        return 0.0f;
    }

    float env = envelope_.process();

    float sample = static_cast<float>(std::sin(phase_));
    phase_ += 2.0 * M_PI * params_.frequency / sampleRate;
    if (phase_ > 2.0 * M_PI) phase_ -= 2.0 * M_PI;

    return sample * params_.amplitude * env;
}

bool Voice::isActive() const {
    return envelope_.isActive();
}

VoiceManager::VoiceManager() {
    for (auto& voice : voices_) {
        voice = nullptr;
    }
}

int VoiceManager::addVoice(const VoiceParams& params) {
    if (voiceCount_ >= kMaxVoices) return -1;

    int id = nextId_++;
    auto* voice = new Voice(id);
    voice->setParams(params);
    voice->noteOn();

    for (auto& v : voices_) {
        if (v == nullptr) {
            v = voice;
            voiceCount_++;
            return id;
        }
    }

    delete voice;
    return -1;
}

bool VoiceManager::removeVoice(int voiceId) {
    for (auto& voice : voices_) {
        if (voice && voice->id() == voiceId) {
            voice->noteOff();
            delete voice;
            voice = nullptr;
            voiceCount_--;
            return true;
        }
    }
    return false;
}

void VoiceManager::noteOn(int voiceId) {
    for (auto* voice : voices_) {
        if (voice && voice->id() == voiceId) {
            voice->noteOn();
            return;
        }
    }
}

void VoiceManager::noteOff(int voiceId) {
    for (auto* voice : voices_) {
        if (voice && voice->id() == voiceId) {
            voice->noteOff();
            return;
        }
    }
}

void VoiceManager::setVoiceParams(int voiceId, const VoiceParams& params) {
    for (auto* voice : voices_) {
        if (voice && voice->id() == voiceId) {
            voice->setParams(params);
            return;
        }
    }
}

int VoiceManager::activeVoiceCount() const {
    return voiceCount_;
}

void VoiceManager::setListenerPosition(float x, float y, float z, float yaw, float pitch) {
    panner_.setListenerPosition(x, y, z, yaw, pitch);
}

void VoiceManager::setMasterFilter(BiquadFilter::Type type, float cutoff, float q) {
    filterType_ = type;
    filterCutoff_ = cutoff;
    filterQ_ = q;
    masterFilter_.setType(type);
    masterFilter_.setCutoff(cutoff);
    masterFilter_.setQ(q);
}

float VoiceManager::process(int32_t sampleRate) {
    float output = 0.0f;

    for (auto& voice : voices_) {
        if (!voice) continue;

        float voiceSample = voice->process(sampleRate);

        panner_.setSourcePosition(
            voice->id() * 0.5f, 0.0f, 1.0f
        );
        PanGains gains = panner_.process();
        float pannedSample = voiceSample * (gains.left + gains.right) * 0.5f;

        output += pannedSample;

        if (voice->isActive() == false) {
            delete voice;
            voice = nullptr;
            voiceCount_--;
        }
    }

    masterFilter_.setCutoff(filterCutoff_);
    masterFilter_.setQ(filterQ_);
    output = masterFilter_.process(output);

    return std::clamp(output, -1.0f, 1.0f);
}
