#ifndef AETHER_BIQUAD_FILTER_H
#define AETHER_BIQUAD_FILTER_H

#include <cstdint>
#include <cmath>

class BiquadFilter {
public:
    enum class Type { LowPass, BandPass };

    explicit BiquadFilter(Type type = Type::LowPass);

    void setType(Type type);
    void setSampleRate(int32_t rate);
    void setCutoff(float frequencyHz);
    void setQ(float resonance);

    float process(float input);
    void reset();

private:
    void calculateCoefficients();

    Type type_;
    int32_t sampleRate_ = 48000;
    float cutoff_ = 1000.0f;
    float q_ = 0.707f;

    float b0_ = 0.0f, b1_ = 0.0f, b2_ = 0.0f;
    float a0_ = 1.0f, a1_ = 0.0f, a2_ = 0.0f;

    float x1_ = 0.0f, x2_ = 0.0f;
    float y1_ = 0.0f, y2_ = 0.0f;
};

#endif
