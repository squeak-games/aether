package com.squeakgames.aether.xr

import androidx.xr.scenecore.SpatialUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HeadTrackingStateProvider(
    spatialUser: SpatialUser,
) {

    private val head = spatialUser.head

    private val _headPose = MutableStateFlow(HeadPose())
    val headPose: Flow<HeadPose> = _headPose.asStateFlow()

    private var isTracking = false

    suspend fun pollForUpdate() {
        val headPose = head?.getActivitySpacePose() ?: return
        _headPose.value = HeadPose(
            x = headPose.translation.x,
            y = headPose.translation.y,
            z = headPose.translation.z,
            yaw = headPose.rotation.y,
            pitch = headPose.rotation.x,
        )
    }

    fun currentPose(): HeadPose = _headPose.value

    data class HeadPose(
        val x: Float = 0f,
        val y: Float = 0f,
        val z: Float = 0f,
        val yaw: Float = 0f,
        val pitch: Float = 0f,
    ) {
        val isOrigin: Boolean get() = x == 0f && y == 0f && z == 0f
    }
}
