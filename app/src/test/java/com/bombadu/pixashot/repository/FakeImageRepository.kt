package com.bombadu.pixashot.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bombadu.pixashot.Resource
import com.bombadu.pixashot.local.LocalData
import com.bombadu.pixashot.network.ImageResponse

/**
 * For Testing the ViewModel
 */

class FakeImageRepository : ImageRepository {

    private val imageData = mutableListOf<LocalData>()
    private val observableImageData = MutableLiveData<List<LocalData>>(imageData)
    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    private fun refreshLiveData() {
        observableImageData.postValue(imageData)
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return if(shouldReturnNetworkError) {
            Resource.error("Error", null)
        } else {
            Resource.success(ImageResponse(listOf(), 0 ,0))
        }
    }

    override suspend fun insertEntry(localData: LocalData) {
        imageData.add(localData)
        refreshLiveData()
    }

    override suspend fun deleteImageItem(localData: LocalData) {
        imageData.remove(localData)
        refreshLiveData()
    }

    override fun observeAllData(): LiveData<List<LocalData>> {
        return observableImageData
    }
}