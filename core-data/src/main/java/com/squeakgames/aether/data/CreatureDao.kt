package com.squeakgames.aether.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CreatureDao {

    @Query("SELECT * FROM creatures ORDER BY lastSessionTimestamp DESC")
    fun observeAll(): Flow<List<CreatureEntity>>

    @Query("SELECT * FROM creatures WHERE id = :id")
    suspend fun getById(id: String): CreatureEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(creature: CreatureEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(creatures: List<CreatureEntity>)

    @Update
    suspend fun update(creature: CreatureEntity)

    @Delete
    suspend fun delete(creature: CreatureEntity)

    @Query("DELETE FROM creatures")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM creatures")
    suspend fun count(): Int

    @Query("SELECT * FROM creatures WHERE state = :state")
    suspend fun getByState(state: com.squeakgames.aether.model.CreatureState): List<CreatureEntity>
}
