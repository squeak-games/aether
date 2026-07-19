package com.squeakgames.aether.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CreatureEntity::class, BondGraphEntity::class, SessionEntity::class, AnchoredAssetEntity::class],
    version = 2,
    exportSchema = false,
)
abstract class AetherDatabase : RoomDatabase() {

    abstract fun creatureDao(): CreatureDao
    abstract fun bondGraphDao(): BondGraphDao
    abstract fun sessionDao(): SessionDao
    abstract fun anchoredAssetDao(): AnchoredAssetDao

    companion object {
        @Volatile private var instance: AetherDatabase? = null

        fun getInstance(context: Context): AetherDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AetherDatabase::class.java,
                    DATABASE_NAME,
                )
                    .fallbackToDestructiveMigration()
                    .build().also { instance = it }
            }
        }

        private const val DATABASE_NAME = "aether.db"
    }
}
