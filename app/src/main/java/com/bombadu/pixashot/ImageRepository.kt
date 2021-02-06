package com.bombadu.pixashot

import androidx.lifecycle.LiveData
import com.bombadu.pixashot.local.LocalData


interface ImageRepository {

    suspend fun searchForImage (imageQuery: String): Resource<ImageResponse>

    suspend fun insertEntry(localData: LocalData)

    suspend fun deleteImageItem(localData: LocalData)

    fun observeAllData(): LiveData<List<LocalData>>
}