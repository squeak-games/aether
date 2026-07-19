package com.squeakgames.aether.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.squeakgames.aether.model.CreatureState
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
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
    fun creatureDao_upsertAndQuery() = runTest {
        val db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AetherDatabase::class.java,
        ).allowMainThreadQueries().build()

        val dao = db.creatureDao()
        val creature = CreatureEntity(
            id = "creature-1",
            species = "EMBER_MOTE",
            state = CreatureState.STABLE,
            bondLevel = 1,
            sessionCount = 5,
            totalCalmTimeMs = 120000L,
            lastSessionTimestamp = 1000L,
            anchorUuid = null,
        )

        dao.upsert(creature)
        val result = dao.getById("creature-1")
        assertNotNull(result)
        assertEquals("creature-1", result?.id)
        assertEquals(CreatureState.STABLE, result?.state)

        db.close()
    }

    @Test
    fun creatureDao_deleteAllClearsTable() = runTest {
        val db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AetherDatabase::class.java,
        ).allowMainThreadQueries().build()

        val dao = db.creatureDao()
        dao.upsert(
            CreatureEntity(
                id = "c1", species = "HUSHLING", state = CreatureState.THRIVING,
                bondLevel = 2, sessionCount = 1, totalCalmTimeMs = 0L,
                lastSessionTimestamp = 1000L, anchorUuid = null,
            )
        )
        assertEquals(1, dao.count())

        dao.deleteAll()
        assertEquals(0, dao.count())

        db.close()
    }

    @Test
    fun bondGraphDao_activeBondCount() = runTest {
        val db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AetherDatabase::class.java,
        ).allowMainThreadQueries().build()

        val dao = db.bondGraphDao()
        dao.upsert(
            BondGraphEntity(
                id = "bond-1", creatureIdA = "c1", creatureIdB = "c2",
                bondStrength = 0.8f, isActive = true, formedTimestamp = 1000L,
            )
        )
        dao.upsert(
            BondGraphEntity(
                id = "bond-2", creatureIdA = "c3", creatureIdB = "c4",
                bondStrength = 0.5f, isActive = false, formedTimestamp = 2000L,
            )
        )

        assertEquals(1, dao.activeBondCount())

        db.close()
    }

    @Test
    fun sessionDao_createsAndCountsSessions() = runTest {
        val db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AetherDatabase::class.java,
        ).allowMainThreadQueries().build()

        val dao = db.sessionDao()
        assertEquals(0, dao.count())

        dao.upsert(
            SessionEntity(
                id = "s1", startTimestamp = 1000L, endTimestamp = null,
                stateSnapshot = null, creatureId = null,
            )
        )
        assertEquals(1, dao.count())

        db.close()
    }

    @Test
    fun anchoredAssetDao_crud() = runTest {
        val db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AetherDatabase::class.java,
        ).allowMainThreadQueries().build()

        val dao = db.anchoredAssetDao()
        assertEquals(0, dao.count())

        dao.upsert(
            AnchoredAssetEntity(
                anchorUuid = "anchor-1", assetId = "creature-1",
                placementTimestamp = 1000L, poseX = 0f, poseY = 0f, poseZ = 0f,
            )
        )
        assertEquals(1, dao.count())

        val result = dao.getByAnchorUuid("anchor-1")
        assertNotNull(result)
        assertEquals("creature-1", result?.assetId)

        db.close()
    }
}
