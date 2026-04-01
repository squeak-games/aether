package com.squeakgames.aether.audio

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class DspLoopTest {

    @Test
    fun buffer_hasDefaultSize() {
        val loop = DspLoop()
        assertEquals(256, loop.process(48000).size)
    }

    @Test
    fun process_returnsSameBufferOnEachCall() {
        val loop = DspLoop(64)
        val first = loop.process(48000)
        val second = loop.process(48000)
        assertNotEquals(
            "consecutive frames should differ (phase advanced)",
            first.contentToString(), second.contentToString(),
        )
        assertArrayEquals(first, second, 0.001f)
    }

    @Test
    fun zeroAmplitude_returnsSilence() {
        val loop = DspLoop(64)
        loop.setAmplitude(0f)
        val result = loop.process(48000)
        for (sample in result) assertEquals(0f, sample, 0.001f)
    }

    @Test
    fun frequency_change_affectsOutput() {
        val loop = DspLoop(256)
        loop.setFrequency(220f)
        val low = loop.process(48000)
        loop.setFrequency(880f)
        val high = loop.process(48000)
        assertNotEquals(
            "different frequencies should produce different frames",
            low.contentToString(), high.contentToString(),
        )
    }

    @Test
    fun amplitude_clamped() {
        val loop = DspLoop(64)
        loop.setAmplitude(2f)
        val result = loop.process(48000)
        for (sample in result) assertNotEquals("should produce output", 0f, sample, 0.001f)
    }

    @Test
    fun reset_clearsPhase() {
        val loop = DspLoop(64)
        loop.process(48000)
        loop.reset()
        val afterReset = loop.process(48000)
        loop.reset()
        val afterSecondReset = loop.process(48000)
        assertArrayEquals(afterReset, afterSecondReset, 0.001f)
    }
}
