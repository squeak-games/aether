package com.squeakgames.aether.xr

import org.junit.Assert.assertEquals
import org.junit.Test

class TouchpadInputRouterTest {

    @Test
    fun router_startsWithNoGesture() {
        val router = TouchpadInputRouter()
        assertEquals(TouchpadInputRouter.GestureType.NONE, router.lastGesture.value)
    }

    @Test
    fun router_reportsTap() {
        val router = TouchpadInputRouter()
        router.onTap()
        assertEquals(TouchpadInputRouter.GestureType.TAP, router.lastGesture.value)
    }

    @Test
    fun router_reportsSwipe() {
        val router = TouchpadInputRouter()
        router.onSwipe(1f, 2f)
        assertEquals(TouchpadInputRouter.GestureType.SWIPE, router.lastGesture.value)
        assertEquals(1f, router.lastSwipeDirectionX.value, 0.001f)
        assertEquals(2f, router.lastSwipeDirectionY.value, 0.001f)
    }

    @Test
    fun router_reportsLongPress() {
        val router = TouchpadInputRouter()
        router.onLongPress()
        assertEquals(TouchpadInputRouter.GestureType.LONG_PRESS, router.lastGesture.value)
    }

    @Test
    fun router_reportsStroke() {
        val router = TouchpadInputRouter()
        router.onStroke(5f, 3f)
        assertEquals(TouchpadInputRouter.GestureType.STROKE, router.lastGesture.value)
        assertEquals(5f, router.lastStrokeLength.value, 0.001f)
        assertEquals(3f, router.lastStrokeAngle.value, 0.001f)
    }
}
