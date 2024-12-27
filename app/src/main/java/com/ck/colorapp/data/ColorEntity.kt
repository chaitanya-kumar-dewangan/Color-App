package com.ck.colorapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "colors")


data class ColorEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val color :String,
    val timeStamp : Long = System.currentTimeMillis()
)