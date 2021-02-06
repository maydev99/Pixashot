package com.bombadu.pixashot.repository


import android.util.Log
import androidx.lifecycle.LiveData
import com.bombadu.pixashot.Resource
import com.bombadu.pixashot.local.LocalDao
import com.bombadu.pixashot.local.LocalData
import com.bombadu.pixashot.network.ImageResponse
import com.bombadu.pixashot.network.PixabayAPI
import javax.inject.Inject
import kotlin.Exception

class DefaultImageRepository @Inject constructor(
    private val localDao: LocalDao,
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

    override suspend fun insertEntry(localData: LocalData) {
        localDao.insertData(localData)
    }

    override suspend fun deleteImageItem(localData: LocalData) {
        localDao.deleteData(localData)
    }

    override fun observeAllData(): LiveData<List<LocalData>> {
        return localDao.observeAllData()
    }
}