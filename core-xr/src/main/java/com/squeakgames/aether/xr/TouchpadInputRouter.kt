package com.squeakgames.aether.xr

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TouchpadInputRouter {

    enum class GestureType {
        NONE,
        TAP,
        SWIPE,
        LONG_PRESS,
        STROKE,
    }

    private val _lastGesture = MutableStateFlow(GestureType.NONE)
    val lastGesture: StateFlow<GestureType> = _lastGesture.asStateFlow()

    private val _lastSwipeDirectionX = MutableStateFlow(0f)
    val lastSwipeDirectionX: StateFlow<Float> = _lastSwipeDirectionX.asStateFlow()

    private val _lastSwipeDirectionY = MutableStateFlow(0f)
    val lastSwipeDirectionY: StateFlow<Float> = _lastSwipeDirectionY.asStateFlow()

    private val _lastStrokeLength = MutableStateFlow(0f)
    val lastStrokeLength: StateFlow<Float> = _lastStrokeLength.asStateFlow()

    private val _lastStrokeAngle = MutableStateFlow(0f)
    val lastStrokeAngle: StateFlow<Float> = _lastStrokeAngle.asStateFlow()

    fun onTap() {
        _lastGesture.value = GestureType.TAP
    }

    fun onSwipe(directionX: Float, directionY: Float) {
        _lastGesture.value = GestureType.SWIPE
        _lastSwipeDirectionX.value = directionX
        _lastSwipeDirectionY.value = directionY
    }

    fun onLongPress() {
        _lastGesture.value = GestureType.LONG_PRESS
    }

    fun onStroke(length: Float, angle: Float) {
        _lastGesture.value = GestureType.STROKE
        _lastStrokeLength.value = length
        _lastStrokeAngle.value = angle
    }
}
