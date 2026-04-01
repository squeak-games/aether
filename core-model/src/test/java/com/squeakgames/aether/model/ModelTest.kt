package com.squeakgames.aether.model

import org.junit.Assert.assertEquals
import org.junit.Test

class ModelTest {

    @Test
    fun creatureState_hasFourValues() {
        val values = CreatureState.values()
        assertEquals(4, values.size)
        assertEquals(CreatureState.THRIVING, values[0])
        assertEquals(CreatureState.STABLE, values[1])
        assertEquals(CreatureState.ENTROPIC, values[2])
        assertEquals(CreatureState.DORMANT, values[3])
    }

    @Test
    fun bondLevel_hasFiveValues() {
        val values = BondLevel.values()
        assertEquals(5, values.size)
        assertEquals(BondLevel.STRANGER, values[0])
        assertEquals(BondLevel.SYMBIOTE, values[4])
    }

    @Test
    fun species_hasFiveValues() {
        val values = Species.values()
        assertEquals(5, values.size)
        assertEquals(Species.EMBER_MOTE, values[0])
        assertEquals(Species.ROOTCLASP, values[4])
    }

    @Test
    fun careAction_hasFiveValues() {
        val values = CareAction.values()
        assertEquals(5, values.size)
        assertEquals(CareAction.OFFERING, values[0])
        assertEquals(CareAction.CALLING, values[4])
    }

    @Test
    fun careAction_offering_hasCorrectWeight() {
        assertEquals(0.05f, CareAction.OFFERING.weight, 0.001f)
    }

    @Test
    fun environmentalContext_hasFourValues() {
        val values = EnvironmentalContext.values()
        assertEquals(4, values.size)
        assertEquals(EnvironmentalContext.CALM, values[0])
        assertEquals(EnvironmentalContext.BASELINE, values[1])
        assertEquals(EnvironmentalContext.ELEVATED, values[2])
        assertEquals(EnvironmentalContext.DISTRESS, values[3])
    }

    @Test
    fun creatureProfile_unlocksPalettes_byBondLevel() {
        val stranger = CreatureProfile(
            id = "1", species = Species.EMBER_MOTE,
            state = CreatureState.STABLE, bondLevel = BondLevel.STRANGER,
            cumulativeCalmTimeMs = 0, sessionCount = 1,
        )
        assertEquals(1, stranger.harmonicPaletteUnlocked)

        val symbiote = stranger.copy(bondLevel = BondLevel.SYMBIOTE)
        assertEquals(5, symbiote.harmonicPaletteUnlocked)
    }
}
