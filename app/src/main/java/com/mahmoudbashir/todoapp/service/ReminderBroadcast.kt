package com.mahmoudbashir.todoapp.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.mahmoudbashir.todoapp.R
import com.mahmoudbashir.todoapp.ToDoApplication
import com.mahmoudbashir.todoapp.dagger.module.RoomDatabaseModule
import com.mahmoudbashir.todoapp.model.DataModel
import com.mahmoudbashir.todoapp.repository.TODOLISTRepository
import com.mahmoudbashir.todoapp.room.TODOLISTDatabase
import com.mahmoudbashir.todoapp.ui.MainActivity
import com.mahmoudbashir.todoapp.viewmodel.TODOViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Flow

class ReminderBroadcast : BroadcastReceiver() {
    lateinit var viewModel : TODOViewModel
    lateinit var repos:TODOLISTRepository
    lateinit var dbase:TODOLISTDatabase
    lateinit var mlist:List<DataModel>
    override fun onReceive(context: Context?, intent: Intent?) {

        val currentDT = SimpleDateFormat("dd/M/yyyy")
        val date = currentDT.format(Date()).toString()

        val df = SimpleDateFormat("hh:mm aa")
        val currentTm:String = df.format(Date())


        repos = TODOLISTRepository(RoomDatabaseModule(ToDoApplication.instance).providesRoomDatabase())
        GlobalScope.launch(Dispatchers.IO) {
                val i = repos.getAllDataListInsideBroadCast()
                for (data in i){
                    val checkDate = data.date?.equals(date) == true
                    val checkTime = data.time?.equals(currentTm) ==true
                    Log.d("Deb:","SavedDate : ${data.date} , currentDate : $date , checkTime : $checkTime" )
                    if (checkDate && checkTime){
                        val title = data.title
                        val desc = data.description
                        remindMe(context,title,desc)
                }
            }
        }
    }

    private fun remindMe(context: Context?,title:String?,desc:String?){

        val alarmsound:Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(
            context!!, "notifyme"
        )
            .setContentTitle(title)
            .setContentText("Hey,you have a reminder for $desc")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSound(alarmsound)
            .setVibrate(longArrayOf(3000))



        val notificationManagerCompat = NotificationManagerCompat.from(context!!)
        notificationManagerCompat.notify(200, builder.build())
    }
}