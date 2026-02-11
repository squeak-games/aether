package com.squeakgames.aether.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey val id: String,
    val startTimestamp: Long,
    val endTimestamp: Long?,
    val stateSnapshot: String?,
    val creatureId: String?,
)
