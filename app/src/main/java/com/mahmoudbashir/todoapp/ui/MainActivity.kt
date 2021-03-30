package com.mahmoudbashir.todoapp.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.mahmoudbashir.todoapp.R
import com.mahmoudbashir.todoapp.ToDoApplication
import com.mahmoudbashir.todoapp.dagger.module.RoomDatabaseModule
import com.mahmoudbashir.todoapp.repository.TODOLISTRepository
import com.mahmoudbashir.todoapp.room.TODOLISTDatabase
import com.mahmoudbashir.todoapp.viewmodel.TODOViewModel
import com.mahmoudbashir.todoapp.viewmodel.TodoViewModelProviderFactory

class MainActivity : AppCompatActivity() {
    lateinit var viewModel:TODOViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.statusBarColor = Color.WHITE
       val navHostFrag = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment

        //val navController = navHostFrag.navController

        val repository = TODOLISTRepository(RoomDatabaseModule(this.application).providesRoomDatabase())
        val viewModelFactory = TodoViewModelProviderFactory(repository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(TODOViewModel::class.java)
    }
}