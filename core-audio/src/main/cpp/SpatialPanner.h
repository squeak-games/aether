#ifndef AETHER_SPATIAL_PANNER_H
#define AETHER_SPATIAL_PANNER_H

#include <cstdint>
#include <cmath>

struct PanGains {
    float left;
    float right;
};

class SpatialPanner {
public:
    SpatialPanner();

    void setListenerPosition(float x, float y, float z, float yaw, float pitch);
    void setSourcePosition(float x, float y, float z);
    void setDistanceRolloff(float factor);

    PanGains process();

private:
    float listenerX_ = 0.0f, listenerY_ = 0.0f, listenerZ_ = 0.0f;
    float listenerYaw_ = 0.0f, listenerPitch_ = 0.0f;
    float sourceX_ = 0.0f, sourceY_ = 0.0f, sourceZ_ = 0.0f;
    float rolloff_ = 1.0f;
};

#endif
