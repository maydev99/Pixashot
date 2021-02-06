package com.bombadu.pixashot.repository

import androidx.lifecycle.LiveData
import com.bombadu.pixashot.Resource
import com.bombadu.pixashot.local.LocalData
import com.bombadu.pixashot.network.ImageResponse


interface ImageRepository {

    suspend fun searchForImage (imageQuery: String): Resource<ImageResponse>

    suspend fun insertEntry(localData: LocalData)

    suspend fun deleteImageItem(localData: LocalData)

    fun observeAllData(): LiveData<List<LocalData>>
}