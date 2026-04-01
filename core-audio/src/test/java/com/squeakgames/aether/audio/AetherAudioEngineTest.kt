package com.squeakgames.aether.audio

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assume.assumeTrue
import org.junit.Test

class AetherAudioEngineTest {

    @Test
    fun engine_returnsVersionString() {
        val engine = AetherAudioEngine()
        assumeTrue("native library not loaded", engine.isNativeLoaded())
        val version = engine.getVersion()
        assertNotNull(version)
        assertEquals("0.0.2-audio-prototype", version)
    }

    @Test
    fun engine_startsIdle() {
        val engine = AetherAudioEngine()
        assertEquals(AudioEngineState.Idle, engine.state.value)
    }

    @Test
    fun voice_hasExpectedFields() {
        val voice = Voice(frequency = 440f, amplitude = 0.5f, pan = 0f)
        assertEquals(440f, voice.frequency, 0.001f)
        assertEquals(0.5f, voice.amplitude, 0.001f)
        assertEquals(0f, voice.pan, 0.001f)
    }

    @Test
    fun spatialSource_hasExpectedFields() {
        val source = SpatialSource(x = 1f, y = 2f, z = -3f)
        assertEquals(1f, source.x, 0.001f)
        assertEquals(2f, source.y, 0.001f)
        assertEquals(-3f, source.z, 0.001f)
    }

    @Test
    fun ringBuffer_returnsZeroSize_whenNativeNotLoaded() {
        val engine = AetherAudioEngine()
        assumeTrue("native library loaded, test not applicable", !engine.isNativeLoaded())
        assertEquals(0, engine.ringBufferSize())
    }

    @Test
    fun pushCommand_returnsFalse_whenNativeNotLoaded() {
        val engine = AetherAudioEngine()
        assumeTrue("native library loaded, test not applicable", !engine.isNativeLoaded())
        assertEquals(false, engine.pushCommand(1, 440f))
    }
}
