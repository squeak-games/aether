package com.squeakgames.aether.xr

import androidx.xr.scenecore.SpatialCapabilities
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CapabilityDetectorTest {

    @Test
    fun noCapabilities_allFalse() {
        val caps = SpatialCapabilities(0)
        val detector = CapabilityDetector(caps)
        assertFalse(detector.hasSpatialUi)
        assertFalse(detector.has3dContent)
        assertFalse(detector.hasPassthroughControl)
        assertFalse(detector.hasAppEnvironment)
        assertFalse(detector.hasSpatialAudio)
        assertFalse(detector.hasEmbedActivity)
    }

    @Test
    fun allCapabilities_allTrue() {
        val mask = SpatialCapabilities.SPATIAL_CAPABILITY_UI or
            SpatialCapabilities.SPATIAL_CAPABILITY_3D_CONTENT or
            SpatialCapabilities.SPATIAL_CAPABILITY_PASSTHROUGH_CONTROL or
            SpatialCapabilities.SPATIAL_CAPABILITY_APP_ENVIRONMENT or
            SpatialCapabilities.SPATIAL_CAPABILITY_SPATIAL_AUDIO or
            SpatialCapabilities.SPATIAL_CAPABILITY_EMBED_ACTIVITY
        val caps = SpatialCapabilities(mask)
        val detector = CapabilityDetector(caps)
        assertTrue(detector.hasSpatialUi)
        assertTrue(detector.has3dContent)
        assertTrue(detector.hasPassthroughControl)
        assertTrue(detector.hasAppEnvironment)
        assertTrue(detector.hasSpatialAudio)
        assertTrue(detector.hasEmbedActivity)
    }

    @Test
    fun headPose_defaultIsOrigin() {
        val pose = HeadTrackingStateProvider.HeadPose()
        assertTrue(pose.isOrigin)
    }

    @Test
    fun headPose_nonOrigin() {
        val pose = HeadTrackingStateProvider.HeadPose(x = 1f, y = 2f, z = 3f)
        assertFalse(pose.isOrigin)
        assertEquals(1f, pose.x, 0.001f)
        assertEquals(2f, pose.y, 0.001f)
        assertEquals(3f, pose.z, 0.001f)
    }
}
