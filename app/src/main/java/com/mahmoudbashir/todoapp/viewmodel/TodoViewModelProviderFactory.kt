package com.mahmoudbashir.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mahmoudbashir.todoapp.repository.TODOLISTRepository
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class TodoViewModelProviderFactory @Inject constructor(private val repository:TODOLISTRepository) :ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
       return TODOViewModel(repository) as T
    }
}