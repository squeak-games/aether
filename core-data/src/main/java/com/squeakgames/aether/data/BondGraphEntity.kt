package com.squeakgames.aether.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "bond_graph")
data class BondGraphEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val creatureIdA: String,
    val creatureIdB: String,
    val bondStrength: Float,
    val isActive: Boolean,
    val formedTimestamp: Long,
)
