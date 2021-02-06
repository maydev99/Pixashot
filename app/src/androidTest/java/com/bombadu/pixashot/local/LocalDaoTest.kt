package com.bombadu.pixashot.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class LocalDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: LocalDatabase
    private lateinit var dao: LocalDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            LocalDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.localDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertImageCorrectData() = runBlockingTest {
        val myData = LocalData("https://www.google.com", "This is google", "Michael May", id = 1)
        dao.insertData(myData) //Tests Insert
        val allLocalData = dao.observeAllData().getOrAwaitValue() //Tests Observe
        assertThat(allLocalData).contains(myData)
    }

    @Test
    fun insertImageDataThenDelete() = runBlockingTest {
        val myData = LocalData("https://www.google.com", "This is google", "Michael May", id = 1)
        dao.insertData(myData)  //Tests Insert
        dao.deleteData(myData) //Tests Delete
        val allLocalData = dao.observeAllData().getOrAwaitValue()
        assertThat(allLocalData).doesNotContain(myData) //Tests Observe
    }

    @Test
    fun insertThreeEntriesDeleteOneCheckDBForDeletedItem() = runBlockingTest {
        val myData1 = LocalData("https://www.google.com", "This is google", "Michael May", id = 1)
        val myData2 = LocalData("https://www.google.com", "This is google", "Michael May", id = 2)
        val myData3 = LocalData("https://www.google.com", "This is google", "Michael May", id = 3)
        dao.insertData(myData1)
        dao.insertData(myData2)
        dao.insertData(myData3)

        dao.deleteData(myData3)

        val allLocalData = dao.observeAllData().getOrAwaitValue()
        assertThat(allLocalData).contains(myData1)
        assertThat(allLocalData).contains(myData2)
        assertThat(allLocalData).doesNotContain(myData3)

    }

    @Test
    fun insertThreeEntriesDeleteTwoCheckDBSize() = runBlockingTest {
        val myData1 = LocalData("https://www.google.com", "This is google", "Michael May", id = 1)
        val myData2 = LocalData("https://www.google.com", "This is google", "Michael May", id = 2)
        val myData3 = LocalData("https://www.google.com", "This is google", "Michael May", id = 3)
        dao.insertData(myData1)
        dao.insertData(myData2)
        dao.insertData(myData3)

        dao.deleteData(myData3)
        dao.deleteData(myData2)

        val allLocalData = dao.observeAllData().getOrAwaitValue()
        assertThat(allLocalData.size).isEqualTo(1)

    }


}