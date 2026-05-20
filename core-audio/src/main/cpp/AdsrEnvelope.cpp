#include "AdsrEnvelope.h"
#include <algorithm>

void AdsrEnvelope::noteOn() {
    state_ = State::Attack;
    currentLevel_ = 0.0f;
}

void AdsrEnvelope::noteOff() {
    if (state_ != State::Idle) {
        state_ = State::Release;
    }
}

void AdsrEnvelope::reset() {
    state_ = State::Idle;
    currentLevel_ = 0.0f;
}

float AdsrEnvelope::process() {
    switch (state_) {
    case State::Idle:
        currentLevel_ = 0.0f;
        break;

    case State::Attack: {
        float increment = 1.0f / (attackTime_ * 0.001f * sampleRate_);
        currentLevel_ += increment;
        if (currentLevel_ >= 1.0f) {
            currentLevel_ = 1.0f;
            state_ = State::Decay;
        }
        break;
    }

    case State::Decay: {
        float decrement = (1.0f - sustainLevel_) / (decayTime_ * 0.001f * sampleRate_);
        currentLevel_ -= decrement;
        if (currentLevel_ <= sustainLevel_) {
            currentLevel_ = sustainLevel_;
            state_ = State::Sustain;
        }
        break;
    }

    case State::Sustain:
        break;

    case State::Release: {
        float decrement = sustainLevel_ / (releaseTime_ * 0.001f * sampleRate_);
        currentLevel_ -= decrement;
        if (currentLevel_ <= 0.0f) {
            currentLevel_ = 0.0f;
            state_ = State::Idle;
        }
        break;
    }
    }

    return currentLevel_;
}
