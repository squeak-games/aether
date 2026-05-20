#ifndef AETHER_ADSR_ENVELOPE_H
#define AETHER_ADSR_ENVELOPE_H

#include <cstdint>

class AdsrEnvelope {
public:
    enum class State { Idle, Attack, Decay, Sustain, Release };

    AdsrEnvelope() = default;

    void setAttack(float timeMs) { attackTime_ = timeMs; }
    void setDecay(float timeMs) { decayTime_ = timeMs; }
    void setSustain(float level) { sustainLevel_ = level; }
    void setRelease(float timeMs) { releaseTime_ = timeMs; }
    void setSampleRate(int32_t rate) { sampleRate_ = rate; }

    void noteOn();
    void noteOff();
    void reset();

    float process();
    State state() const { return state_; }
    bool isActive() const { return state_ != State::Idle; }

private:
    State state_ = State::Idle;
    float attackTime_ = 10.0f;
    float decayTime_ = 50.0f;
    float sustainLevel_ = 0.7f;
    float releaseTime_ = 100.0f;
    int32_t sampleRate_ = 48000;
    float currentLevel_ = 0.0f;
};

#endif
