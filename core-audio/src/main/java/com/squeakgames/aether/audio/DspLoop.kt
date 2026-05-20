package com.squeakgames.aether.audio

class DspLoop(
    private val bufferSize: Int = DEFAULT_BUFFER_SIZE,
) {

    private val outputBuffer: FloatArray = FloatArray(bufferSize)
    private var phase: Double = 0.0
    private var currentFrequency: Float = 440f
    private var currentAmplitude: Float = 0.5f

    fun setFrequency(hz: Float) { currentFrequency = hz }
    fun setAmplitude(amp: Float) { currentAmplitude = amp.coerceIn(0f, 1f) }

    fun process(sampleRate: Int): FloatArray {
        val freq = currentFrequency
        val amp = currentAmplitude
        val buffer = outputBuffer
        val step = (2.0 * Math.PI * freq) / sampleRate

        for (i in buffer.indices) {
            buffer[i] = (Math.sin(phase) * amp).toFloat()
            phase += step
            if (phase > 2.0 * Math.PI) phase -= 2.0 * Math.PI
        }

        return buffer
    }

    fun reset() {
        phase = 0.0
        for (i in outputBuffer.indices) outputBuffer[i] = 0f
    }

    companion object {
        const val DEFAULT_BUFFER_SIZE = 256
    }
}
