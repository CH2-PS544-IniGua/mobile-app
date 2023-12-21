package com.tri.sulton.inigua.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tri.sulton.inigua.data.api.model.response.CatalogItem

@Database(
    entities = [CatalogItem::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class IniGuaDatabase : RoomDatabase() {

    abstract fun productDao(): CatalogDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: IniGuaDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): IniGuaDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    IniGuaDatabase::class.java, "inigua_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}