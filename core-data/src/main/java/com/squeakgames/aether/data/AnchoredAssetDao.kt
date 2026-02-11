package com.squeakgames.aether.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface AnchoredAssetDao {

    @Query("SELECT * FROM anchored_assets WHERE anchorUuid = :anchorUuid")
    suspend fun getByAnchorUuid(anchorUuid: String): AnchoredAssetEntity?

    @Query("SELECT * FROM anchored_assets WHERE assetId = :assetId")
    suspend fun getByAssetId(assetId: String): List<AnchoredAssetEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(asset: AnchoredAssetEntity)

    @Update
    suspend fun update(asset: AnchoredAssetEntity)

    @Delete
    suspend fun delete(asset: AnchoredAssetEntity)

    @Query("DELETE FROM anchored_assets")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM anchored_assets")
    suspend fun count(): Int
}
