package com.squeakgames.aether.xr

import androidx.xr.scenecore.SpatialCapabilities
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SpatialCapabilitiesProviderTest {

    @Test
    fun noCapabilities_allFalse() {
        val caps = SpatialCapabilities(0)
        val provider = SpatialCapabilitiesProvider(caps)
        assertFalse(provider.hasSpatialUi)
        assertFalse(provider.has3dContent)
        assertFalse(provider.hasPassthroughControl)
        assertFalse(provider.hasAppEnvironment)
        assertFalse(provider.hasSpatialAudio)
        assertFalse(provider.hasEmbedActivity)
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
        val provider = SpatialCapabilitiesProvider(caps)
        assertTrue(provider.hasSpatialUi)
        assertTrue(provider.has3dContent)
        assertTrue(provider.hasPassthroughControl)
        assertTrue(provider.hasAppEnvironment)
        assertTrue(provider.hasSpatialAudio)
        assertTrue(provider.hasEmbedActivity)
    }

    @Test
    fun isSpatialAudioEnabled_returnsTrue_whenCapabilityPresent() {
        val mask = SpatialCapabilities.SPATIAL_CAPABILITY_SPATIAL_AUDIO
        val caps = SpatialCapabilities(mask)
        val provider = SpatialCapabilitiesProvider(caps)
        assertTrue(provider.isSpatialAudioEnabled)
    }

    @Test
    fun isSpatialAudioEnabled_returnsFalse_whenCapabilityMissing() {
        val caps = SpatialCapabilities(0)
        val provider = SpatialCapabilitiesProvider(caps)
        assertFalse(provider.isSpatialAudioEnabled)
    }

    @Test
    fun isHandTrackingEnabled_returnsTrue_whenCapabilityPresent() {
        val mask = SpatialCapabilities.SPATIAL_CAPABILITY_UI or
            SpatialCapabilities.SPATIAL_CAPABILITY_3D_CONTENT or
            SpatialCapabilities.SPATIAL_CAPABILITY_APP_ENVIRONMENT
        val caps = SpatialCapabilities(mask)
        val provider = SpatialCapabilitiesProvider(caps)
        assertTrue(provider.isHandTrackingEnabled)
    }

    @Test
    fun isHandTrackingEnabled_returnsFalse_whenCapabilitiesLimited() {
        val caps = SpatialCapabilities(SpatialCapabilities.SPATIAL_CAPABILITY_SPATIAL_AUDIO)
        val provider = SpatialCapabilitiesProvider(caps)
        assertFalse(provider.isHandTrackingEnabled)
    }
}
