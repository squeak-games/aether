package com.squeakgames.aether.model

data class EnvironmentalStressIndex(
    val noise: Float,
    val motion: Float,
    val locationChange: Float,
    val composite: Float,
)
