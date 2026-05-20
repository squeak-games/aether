#include "SpatialPanner.h"
#include <algorithm>

SpatialPanner::SpatialPanner() = default;

void SpatialPanner::setListenerPosition(float x, float y, float z, float yaw, float pitch) {
    listenerX_ = x;
    listenerY_ = y;
    listenerZ_ = z;
    listenerYaw_ = yaw;
    listenerPitch_ = pitch;
}

void SpatialPanner::setSourcePosition(float x, float y, float z) {
    sourceX_ = x;
    sourceY_ = y;
    sourceZ_ = z;
}

void SpatialPanner::setDistanceRolloff(float factor) {
    rolloff_ = factor;
}

PanGains SpatialPanner::process() {
    float dx = sourceX_ - listenerX_;
    float dy = sourceY_ - listenerY_;
    float dz = sourceZ_ - listenerZ_;

    float distance = std::sqrt(dx * dx + dy * dy + dz * dz);
    float distanceAttenuation = 1.0f / std::max(1.0f, distance * rolloff_);

    float angle = std::atan2(dx, dz) - listenerYaw_;
    while (angle > static_cast<float>(M_PI)) angle -= 2.0f * static_cast<float>(M_PI);
    while (angle < -static_cast<float>(M_PI)) angle += 2.0f * static_cast<float>(M_PI);

    float pan = std::sin(angle * 0.5f);
    pan = std::clamp(pan, -1.0f, 1.0f);

    PanGains gains;
    gains.left = distanceAttenuation * std::min(1.0f, 1.0f - pan);
    gains.right = distanceAttenuation * std::min(1.0f, 1.0f + pan);

    return gains;
}
