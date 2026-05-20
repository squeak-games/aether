#include "BiquadFilter.h"

BiquadFilter::BiquadFilter(Type type) : type_(type) {
    calculateCoefficients();
}

void BiquadFilter::setType(Type type) {
    type_ = type;
    calculateCoefficients();
}

void BiquadFilter::setSampleRate(int32_t rate) {
    sampleRate_ = rate;
    calculateCoefficients();
}

void BiquadFilter::setCutoff(float frequencyHz) {
    cutoff_ = frequencyHz;
    calculateCoefficients();
}

void BiquadFilter::setQ(float resonance) {
    q_ = resonance;
    calculateCoefficients();
}

void BiquadFilter::calculateCoefficients() {
    float w0 = 2.0f * static_cast<float>(M_PI) * cutoff_ / sampleRate_;
    float alpha = std::sin(w0) / (2.0f * q_);
    float cosW0 = std::cos(w0);

    if (type_ == Type::LowPass) {
        b0_ = (1.0f - cosW0) / 2.0f;
        b1_ = 1.0f - cosW0;
        b2_ = (1.0f - cosW0) / 2.0f;
    } else {
        b0_ = alpha;
        b1_ = 0.0f;
        b2_ = -alpha;
    }

    float a0 = 1.0f + alpha;
    a1_ = -2.0f * cosW0;
    a2_ = 1.0f - alpha;

    b0_ /= a0;
    b1_ /= a0;
    b2_ /= a0;
    a1_ /= a0;
    a2_ /= a0;
}

float BiquadFilter::process(float input) {
    float output = b0_ * input + b1_ * x1_ + b2_ * x2_ - a1_ * y1_ - a2_ * y2_;

    x2_ = x1_;
    x1_ = input;
    y2_ = y1_;
    y1_ = output;

    return output;
}

void BiquadFilter::reset() {
    x1_ = x2_ = y1_ = y2_ = 0.0f;
}
