package com.mahmoudbashir.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.mahmoudbashir.todoapp.model.DataModel
import com.mahmoudbashir.todoapp.repository.TODOLISTRepository
import kotlinx.coroutines.launch

public class TODOViewModel( private val repository:TODOLISTRepository):ViewModel() {

    val getAllList:LiveData<List<DataModel>> = repository.getAllDataList

    fun insert(data: DataModel) = viewModelScope.launch {
        repository.insert(data)
    }

    fun deleteDataFromList(data: DataModel) = viewModelScope.launch {
        repository.deleteDataFromList(data)
    }

}