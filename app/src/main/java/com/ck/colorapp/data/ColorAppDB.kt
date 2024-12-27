package com.ck.colorapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ColorEntity::class], version = 1)
abstract class ColorAppDB : RoomDatabase(){
    abstract fun colorDao() : ColorDao
}