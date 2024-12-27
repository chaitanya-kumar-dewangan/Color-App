package com.ck.colorapp.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ColorDao {

    @Upsert
    suspend  fun addColor(color : ColorEntity)

    @Query("SELECT * FROM colors")
     fun getColors(): Flow<List<ColorEntity>>
}