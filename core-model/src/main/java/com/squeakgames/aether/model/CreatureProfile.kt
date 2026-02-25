package com.squeakgames.aether.model

data class CreatureProfile(
    val id: String,
    val species: Species,
    val state: CreatureState,
    val bondLevel: BondLevel,
    val cumulativeCalmTimeMs: Long,
    val sessionCount: Int,
) {
    val harmonicPaletteUnlocked: Int
        get() = when (bondLevel) {
            BondLevel.STRANGER -> 1
            BondLevel.FAMILIAR -> 2
            BondLevel.COMPANION -> 3
            BondLevel.BONDED -> 4
            BondLevel.SYMBIOTE -> 5
        }
}
