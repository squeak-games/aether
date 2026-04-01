package com.squeakgames.aether.model

enum class CareAction(
    val label: String,
    val weight: Float,
) {
    OFFERING("Open palm offering", 0.05f),
    SHELTERING("Cupped hands shelter", 0.08f),
    BUILDING("Pinch and place", 0.03f),
    GUIDING("Gentle reach guiding", 0.06f),
    CALLING("Vocal resonance calling", 0.10f),
}
