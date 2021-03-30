package com.mahmoudbashir.todoapp.dagger.component

import com.mahmoudbashir.todoapp.dagger.module.RoomDatabaseModule
import com.mahmoudbashir.todoapp.fragments.HomeList_Fragment
import com.mahmoudbashir.todoapp.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RoomDatabaseModule::class])
interface ToDoComponent {
    fun inject(home:HomeList_Fragment)
}