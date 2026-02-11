package com.squeakgames.aether.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anchored_assets")
data class AnchoredAssetEntity(
    @PrimaryKey val anchorUuid: String,
    val assetId: String,
    val placementTimestamp: Long,
    val poseX: Float,
    val poseY: Float,
    val poseZ: Float,
)
