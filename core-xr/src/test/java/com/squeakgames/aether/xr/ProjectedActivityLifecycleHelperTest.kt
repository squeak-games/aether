package com.squeakgames.aether.xr

import org.junit.Assert.assertEquals
import org.junit.Test

class ProjectedActivityLifecycleHelperTest {

    @Test
    fun helper_startsWithActivityState() {
        val helper = ProjectedActivityLifecycleHelper()
        assertEquals(ProjectedActivityLifecycleHelper.ActivityState.DONNED, helper.activityState.value)
    }

    @Test
    fun helper_transitionsToDoffed() {
        val helper = ProjectedActivityLifecycleHelper()
        helper.onDoffed()
        assertEquals(ProjectedActivityLifecycleHelper.ActivityState.DOFFED, helper.activityState.value)
    }

    @Test
    fun helper_transitionsToPaused() {
        val helper = ProjectedActivityLifecycleHelper()
        helper.onPaused()
        assertEquals(ProjectedActivityLifecycleHelper.ActivityState.PAUSED, helper.activityState.value)
    }

    @Test
    fun helper_returnsToDonnedAfterDoffed() {
        val helper = ProjectedActivityLifecycleHelper()
        helper.onDoffed()
        helper.onDonned()
        assertEquals(ProjectedActivityLifecycleHelper.ActivityState.DONNED, helper.activityState.value)
    }
}
