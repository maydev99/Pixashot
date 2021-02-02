package com.bombadu.pixashot

import com.bombadu.pixashot.local.LocalData


interface ImageRepository {

    suspend fun searchForImage (imageQuery: String): Resource<ImageResponse>

    suspend fun insertEntry(localData: LocalData)
}