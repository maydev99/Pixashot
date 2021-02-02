package com.bombadu.pixashot



interface ImageRepository {

    suspend fun searchForImage (imageQuery: String): Resource<ImageResponse>
}