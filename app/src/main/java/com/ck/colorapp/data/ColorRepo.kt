package com.ck.colorapp.data

import kotlinx.coroutines.flow.Flow

class ColorRepo(private val colorDao: ColorDao) {

    suspend fun getColors(): Flow<List<ColorEntity>> = colorDao.getColors()

    suspend fun addColor(color: ColorEntity) = colorDao.addColor(color)
}