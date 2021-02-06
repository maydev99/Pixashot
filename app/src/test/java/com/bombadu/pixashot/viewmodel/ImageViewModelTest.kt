package com.bombadu.pixashot.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bombadu.pixashot.MainCoroutineRule
import com.bombadu.pixashot.Status
import com.bombadu.pixashot.local.LocalData
import com.bombadu.pixashot.repository.FakeImageRepository
import com.bombadu.pixashot.util.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ImageViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ImageViewModel

    @Before
    fun setup() {
        viewModel = ImageViewModel(FakeImageRepository())
    }

    @Test
    fun `insert image item with empty field returns success`(){
        viewModel.insertImageData(LocalData("https://google.com", "Great", "Michael"))
        val value = viewModel.insertImageItemStatus.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)

    }

}