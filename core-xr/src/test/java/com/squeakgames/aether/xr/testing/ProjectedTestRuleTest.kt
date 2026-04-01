package com.squeakgames.aether.xr.testing

import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ProjectedTestRuleTest {

    private val items = mutableListOf("item")

    @get:Rule
    val rule = ProjectedTestRule(items) { list ->
        list.clear()
    }

    @Test
    fun rule_providesWorld() {
        assertTrue(rule.world.contains("item"))
    }
}
