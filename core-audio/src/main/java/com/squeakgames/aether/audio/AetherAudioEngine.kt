package com.squeakgames.aether.audio

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AetherAudioEngine {

    private val _state = MutableStateFlow(AudioEngineState.STOPPED)
    val state: StateFlow<AudioEngineState> = _state.asStateFlow()

    fun start(): Boolean {
        val ok = nativeStart()
        _state.value = if (ok) AudioEngineState.PLAYING else AudioEngineState.ERROR
        return ok
    }

    fun stop(): Boolean {
        val ok = nativeStop()
        _state.value = if (ok) AudioEngineState.STOPPED else AudioEngineState.ERROR
        return ok
    }

    fun setFrequency(hz: Float) {
        nativeSetFrequency(hz)
    }

    fun getFrequency(): Float = nativeGetFrequency()

    fun getVersion(): String = nativeGetVersion()

    fun pushCommand(command: Int, value: Float): Boolean {
        if (!nativeLoaded) return false
        return nativePushCommand(command, value)
    }

    fun popCommand(): FloatArray? {
        if (!nativeLoaded) return null
        return nativePopCommand()
    }

    fun ringBufferSize(): Int {
        if (!nativeLoaded) return 0
        return nativeRingBufferSize()
    }

    companion object {
        private var nativeLoaded = false
        init {
            try {
                System.loadLibrary("aether-audio")
                nativeLoaded = true
            } catch (_: UnsatisfiedLinkError) {
                nativeLoaded = false
            }
        }
    }

    private external fun nativeStart(): Boolean
    private external fun nativeStop(): Boolean
    private external fun nativeSetFrequency(hz: Float)
    private external fun nativeGetFrequency(): Float
    private external fun nativeGetVersion(): String
    private external fun nativePushCommand(command: Int, value: Float): Boolean
    private external fun nativePopCommand(): FloatArray?
    private external fun nativeRingBufferSize(): Int

    fun isNativeLoaded(): Boolean = nativeLoaded
}
