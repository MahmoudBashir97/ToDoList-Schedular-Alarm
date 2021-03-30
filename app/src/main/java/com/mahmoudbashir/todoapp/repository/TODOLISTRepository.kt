package com.mahmoudbashir.todoapp.repository

import androidx.lifecycle.LiveData
import com.mahmoudbashir.todoapp.model.DataModel
import com.mahmoudbashir.todoapp.room.TODOLISTDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class TODOLISTRepository(
        private val db:TODOLISTDatabase
) {
    suspend fun insert(data:DataModel) = db.todoDao().insert(data)

    val  getAllDataList: LiveData<List<DataModel>> = db.todoDao().getAllDataList()

    suspend fun getAllDataListInsideBroadCast():List<DataModel> = db.todoDao().getAllDataListForbroadCast()

    suspend fun deleteDataFromList(data: DataModel) = db.todoDao().deleteDataFromList(data)
}