package com.bombadu.pixashot.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bombadu.pixashot.util.Event
import com.bombadu.pixashot.repository.ImageRepository
import com.bombadu.pixashot.network.ImageResponse
import com.bombadu.pixashot.Resource
import com.bombadu.pixashot.local.LocalData
import kotlinx.coroutines.launch

class ImageViewModel @ViewModelInject constructor(
    private val repository: ImageRepository
) : ViewModel() {

    val savedData = repository.observeAllData()

    private val _images = MutableLiveData<Event<Resource<ImageResponse>>>()
    val images: LiveData<Event<Resource<ImageResponse>>> = _images

    fun searchForImage(imageQuery: String) {
        if (imageQuery.isEmpty()) {
            return
        }

        _images.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.searchForImage(imageQuery)
            _images.value = Event(response)
        }
    }

    fun insertImageData(localData: LocalData) = viewModelScope.launch {
        repository.insertEntry(localData)
    }

    fun deleteImage(localData: LocalData) = viewModelScope.launch {
        repository.deleteImageItem(localData)
    }
}