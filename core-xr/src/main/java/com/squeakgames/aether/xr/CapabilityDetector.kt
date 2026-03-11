package com.squeakgames.aether.xr

import androidx.xr.scenecore.SpatialCapabilities

class CapabilityDetector(capabilities: SpatialCapabilities) {

    val hasSpatialUi: Boolean = capabilities.hasCapability(SpatialCapabilities.SPATIAL_CAPABILITY_UI)
    val has3dContent: Boolean = capabilities.hasCapability(SpatialCapabilities.SPATIAL_CAPABILITY_3D_CONTENT)
    val hasPassthroughControl: Boolean = capabilities.hasCapability(SpatialCapabilities.SPATIAL_CAPABILITY_PASSTHROUGH_CONTROL)
    val hasAppEnvironment: Boolean = capabilities.hasCapability(SpatialCapabilities.SPATIAL_CAPABILITY_APP_ENVIRONMENT)
    val hasSpatialAudio: Boolean = capabilities.hasCapability(SpatialCapabilities.SPATIAL_CAPABILITY_SPATIAL_AUDIO)
    val hasEmbedActivity: Boolean = capabilities.hasCapability(SpatialCapabilities.SPATIAL_CAPABILITY_EMBED_ACTIVITY)
}
