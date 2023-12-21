package com.tri.sulton.inigua.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tri.sulton.inigua.data.api.model.response.CatalogItem

@Dao
interface CatalogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCatalog(catalog: List<CatalogItem>)

    @Query("SELECT * FROM catalog")
    fun getAllCatalog(): PagingSource<Int, CatalogItem>

    @Query("DELETE FROM catalog")
    suspend fun deleteAll()
}