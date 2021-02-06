package com.bombadu.pixashot.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LocalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(localData: LocalData)

    @Delete
    suspend fun deleteData(localData: LocalData)

    @Query("SELECT * FROM data_table ORDER BY id DESC")
    fun observeAllData(): LiveData<List<LocalData>>
}