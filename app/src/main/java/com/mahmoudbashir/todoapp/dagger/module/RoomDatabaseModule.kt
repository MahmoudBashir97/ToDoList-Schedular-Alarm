package com.mahmoudbashir.todoapp.dagger.module

import android.app.Application
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mahmoudbashir.todoapp.room.TODOLISTDatabase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
class RoomDatabaseModule(application: Application) {

    private var todoListApplication = application
    private lateinit var todoDatabase:TODOLISTDatabase

    private val databaseCallback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Log.d("RoomDatabaseModule", "onCreate")
            CoroutineScope(Dispatchers.IO).launch {
            }
        }
    }

    @Singleton
    @Provides
    fun providesRoomDatabase():TODOLISTDatabase{
        todoDatabase = Room.databaseBuilder(todoListApplication,TODOLISTDatabase::class.java,"\"todoList_db\"")
            .fallbackToDestructiveMigration()
            .addCallback(databaseCallback)
            .build()

        return todoDatabase
    }

    @Singleton
    @Provides
    fun providesToDoDao(db:TODOLISTDatabase) = db.todoDao()
}