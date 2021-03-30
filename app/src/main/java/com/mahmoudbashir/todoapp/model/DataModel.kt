package com.mahmoudbashir.todoapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "list_table")
data class DataModel (
        @PrimaryKey(autoGenerate = true)
        var id:Int?=null,
        var title:String?,
        var description:String?,
        var date:String?,
        var time:String?)