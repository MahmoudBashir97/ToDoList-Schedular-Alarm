package com.mahmoudbashir.todoapp.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mahmoudbashir.todoapp.model.DataModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data:DataModel)

    @Query("SELECT * FROM list_table ORDER BY id ASC")
    fun getAllDataList():LiveData<List<DataModel>>

    @Query("SELECT * FROM list_table ORDER BY id ASC")
    suspend fun getAllDataListForbroadCast():List<DataModel>

    @Delete
    suspend fun deleteDataFromList(data:DataModel)
}