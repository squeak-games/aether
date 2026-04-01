package com.squeakgames.aether.xr.testing

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class ProjectedTestRule<T : Any>(
    val world: T,
    private val onCleanup: (T) -> Unit = {},
) : TestRule {

    constructor(world: T) : this(world, {})

    override fun apply(base: Statement, description: Description): Statement =
        object : Statement() {
            override fun evaluate() {
                try {
                    base.evaluate()
                } finally {
                    onCleanup(world)
                }
            }
        }
}
