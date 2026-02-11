package com.squeakgames.aether.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BondGraphDao {

    @Query("SELECT * FROM bond_graph WHERE creatureIdA = :creatureId OR creatureIdB = :creatureId")
    fun observeForCreature(creatureId: String): Flow<List<BondGraphEntity>>

    @Query("SELECT * FROM bond_graph WHERE id = :id")
    suspend fun getById(id: String): BondGraphEntity?

    @Query("SELECT * FROM bond_graph WHERE isActive = 1")
    suspend fun getActiveBonds(): List<BondGraphEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(bond: BondGraphEntity)

    @Update
    suspend fun update(bond: BondGraphEntity)

    @Delete
    suspend fun delete(bond: BondGraphEntity)

    @Query("DELETE FROM bond_graph")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM bond_graph WHERE isActive = 1")
    suspend fun activeBondCount(): Int
}
