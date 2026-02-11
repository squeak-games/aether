package com.squeakgames.aether.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squeakgames.aether.model.CreatureState

@Entity(tableName = "creatures")
data class CreatureEntity(
    @PrimaryKey val id: String,
    val species: String,
    val state: CreatureState,
    val bondLevel: Int,
    val sessionCount: Int,
    val totalCalmTimeMs: Long,
    val lastSessionTimestamp: Long,
    val anchorUuid: String?,
)
