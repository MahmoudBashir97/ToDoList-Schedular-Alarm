package com.mahmoudbashir.todoapp

import android.app.Application
import com.mahmoudbashir.todoapp.dagger.component.DaggerToDoComponent
import com.mahmoudbashir.todoapp.dagger.component.ToDoComponent
import com.mahmoudbashir.todoapp.dagger.module.RoomDatabaseModule

class ToDoApplication : Application() {

    companion object{
        public lateinit var instance:ToDoApplication
    }

    lateinit var todoComp : ToDoComponent
    override fun onCreate() {
        super.onCreate()
        instance = this

        todoComp = DaggerToDoComponent
            .builder()
            .roomDatabaseModule(RoomDatabaseModule(this))
            .build()
    }
}