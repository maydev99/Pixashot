package com.bombadu.pixashot


import android.util.Log
import javax.inject.Inject
import kotlin.Exception

class DefaultImageRepository @Inject constructor(
    private val pixabayAPI: PixabayAPI
) : ImageRepository {
    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return try {
            val response = pixabayAPI.searchForImage(imageQuery)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An Unknown Error Occurred", null)
            } else {
                Resource.error("An Unknown Error Occurred", null)
            }
        } catch (e: Exception) {
            Log.e("EXCEPTION", "EXCEPTION:", e)
            Resource.error("Couldn't reach the server. Check your internet connection", null)
        }
    }
}