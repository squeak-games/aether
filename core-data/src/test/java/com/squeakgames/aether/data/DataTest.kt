package com.squeakgames.aether.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class DataTest {

    @Test
    fun sessionEntity_hasRequiredFields() {
        val entity = SessionEntity(
            id = "session-1",
            startTimestamp = 1000L,
            endTimestamp = null,
            stateSnapshot = null,
            creatureId = null,
        )
        assertEquals("session-1", entity.id)
        assertEquals(1000L, entity.startTimestamp)
        assertNull(entity.endTimestamp)
        assertNull(entity.stateSnapshot)
        assertNull(entity.creatureId)
    }

    @Test
    fun sessionEntity_withEndTimestamp() {
        val entity = SessionEntity(
            id = "session-2",
            startTimestamp = 1000L,
            endTimestamp = 2000L,
            stateSnapshot = "{\"state\":\"THRIVING\"}",
            creatureId = "creature-1",
        )
        assertEquals(2000L, entity.endTimestamp)
        assertEquals("{\"state\":\"THRIVING\"}", entity.stateSnapshot)
        assertEquals("creature-1", entity.creatureId)
    }

    @Test
    fun anchoredAssetEntity_hasRequiredFields() {
        val entity = AnchoredAssetEntity(
            anchorUuid = "anchor-uuid-1",
            assetId = "creature-1",
            placementTimestamp = 1000L,
            poseX = 1.0f,
            poseY = 2.0f,
            poseZ = 3.0f,
        )
        assertEquals("anchor-uuid-1", entity.anchorUuid)
        assertEquals("creature-1", entity.assetId)
        assertEquals(1000L, entity.placementTimestamp)
        assertEquals(1.0f, entity.poseX, 0.001f)
        assertEquals(2.0f, entity.poseY, 0.001f)
        assertEquals(3.0f, entity.poseZ, 0.001f)
    }

    @Test
    fun aetherDatabase_hasExpectedName() {
        assertEquals("aether.db", "aether.db")
    }
}
