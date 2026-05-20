package com.squeakgames.aether.audio

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DspLoopTest {

    @Test
    fun buffer_hasDefaultSize() {
        val loop = DspLoop()
        assertEquals(256, loop.process(48000).size)
    }

    @Test
    fun process_advancesPhaseOnEachCall() {
        val loop = DspLoop(64)
        val buffer = loop.process(48000)
        val firstSample = buffer[0]
        loop.process(48000)
        assertTrue(
            "phase should advance between calls",
            firstSample != buffer[0],
        )
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
        val buffer = loop.process(48000)
        val lowSample = buffer[0]
        loop.setFrequency(880f)
        loop.process(48000)
        assertTrue(
            "different frequencies should produce different output",
            lowSample != buffer[0],
        )
    }

    @Test
    fun amplitude_clamped() {
        val loop = DspLoop(64)
        loop.setAmplitude(2f)
        val result = loop.process(48000)
        val energy = result.sumOf { (it * it).toDouble() }
        assertTrue("clamped amplitude should produce non-zero energy", energy > 0.0)
    }

    @Test
    fun reset_clearsPhase() {
        val loop = DspLoop(64)
        loop.process(48000)
        loop.reset()
        val firstFrame = loop.process(48000).copyOf()
        loop.reset()
        val secondFrame = loop.process(48000).copyOf()
        assertArrayEquals("reset should restart from same phase", firstFrame, secondFrame, 0.001f)
    }
}
