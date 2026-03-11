package com.squeakgames.aether.xr

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProjectedActivityLifecycleHelper {

    enum class ActivityState {
        DONNED,
        DOFFED,
        PAUSED,
    }

    private val _activityState = MutableStateFlow(ActivityState.DONNED)
    val activityState: StateFlow<ActivityState> = _activityState.asStateFlow()

    fun onDonned() {
        _activityState.value = ActivityState.DONNED
    }

    fun onDoffed() {
        _activityState.value = ActivityState.DOFFED
    }

    fun onPaused() {
        _activityState.value = ActivityState.PAUSED
    }
}
