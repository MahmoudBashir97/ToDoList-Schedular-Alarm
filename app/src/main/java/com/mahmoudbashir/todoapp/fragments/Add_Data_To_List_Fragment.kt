package com.mahmoudbashir.todoapp.fragments

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mahmoudbashir.todoapp.R
import com.mahmoudbashir.todoapp.databinding.FragmentAddDataToListBinding
import com.mahmoudbashir.todoapp.model.DataModel
import com.mahmoudbashir.todoapp.service.ReminderBroadcast
import com.mahmoudbashir.todoapp.ui.MainActivity
import com.mahmoudbashir.todoapp.viewmodel.TODOViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class Add_Data_To_List_Fragment : Fragment() {

    lateinit var addToListBinding:FragmentAddDataToListBinding
    lateinit var viewModel:TODOViewModel
    private lateinit var selectedDateTime:String
    private lateinit var date:String
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        addToListBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add__data__to__list,
            container,
            false
        )
        // ViewModel initialization
        viewModel = (activity as MainActivity).viewModel

        // open calendar view for select reminder date
        addToListBinding.selectCalBtn.setOnClickListener {dateSelection()}
        // save buton for saving reminder details / data to database
        addToListBinding.saveBtn.setOnClickListener{
            setData()
            createNotificationChannel()
        }

        return addToListBinding.root
    }


    private fun dateSelection(){

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        // want to notice u that calendar return value of previous month so i set it manually ,,,,, as we are in March
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this.requireContext(),
            DatePickerDialog.OnDateSetListener { _, myear, mMonth, mDay ->
                if (myear >= 2021) {

                    val d = mDay.toString()
                    val m = mMonth.toString()
                    val y = myear.toString()
                    date = "$d/$m/$y"

                    val currentTm = SimpleDateFormat("hh:mm:ss aa")
                    selectedDateTime = "$date   ${currentTm.format(Date())}"

                    if (!selectedDateTime.equals(null)) {
                        addToListBinding.edtDate.setText(selectedDateTime)
                    }
                }
            }, year, 3, day
        )
        dpd.show()
    }

    private fun setData(){
        when {
            TextUtils.isEmpty(addToListBinding.edtTitle.text.toString()) -> {
                addToListBinding.edtTitle.error = "Please Enter Your Reminder Title"
                addToListBinding.edtTitle.requestFocus()
            }
            TextUtils.isEmpty(addToListBinding.edtDesc.text.toString()) -> {
                addToListBinding.edtDesc.error = "Please Enter Your Reminder Description"
                addToListBinding.edtDesc.requestFocus()
            }
            TextUtils.isEmpty(addToListBinding.edtDate.text.toString())->{
                addToListBinding.edtDate.error = "Please Select  Date of your Reminder"
                addToListBinding.edtDate.requestFocus()
            }
            else -> {

                val title = addToListBinding.edtTitle.text.toString()
                val desc  = addToListBinding.edtDesc.text.toString()

                val df = SimpleDateFormat("hh:mm aa")
                val cal = Calendar.getInstance()
                cal.add(Calendar.MINUTE, -10)
                val newTime: String = df.format(cal.time)

                saveToRoom(title, desc, newTime)
            }
        }
    }

   private fun saveToRoom(title: String, desc: String, time: String){
       addToListBinding.isLoading = true
       Log.d("Deb:", "data : $time")

       val data  = DataModel(title = title, description = desc, date = date, time = time)
        GlobalScope.launch {
            delay(200)
            val check:Boolean = viewModel.insert(data).isActive.and(isAdded)
            if (check){
                addToListBinding.isLoading = false
                passToBroadCast()
                findNavController().navigateUp()
                Log.d("dataSaved", "completed")

            }
        }
    }

    private fun passToBroadCast(){
        val i = Intent(context, ReminderBroadcast::class.java).let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, 0)
        }
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as?AlarmManager
        alarmManager?.setRepeating(
            AlarmManager.RTC_WAKEUP,
            SystemClock.elapsedRealtime(), 3000,
            i
        )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "MahmoudChannel"
            val description = "Channel for Mahmoud Reminder"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("notifyme", name, importance)
            channel.description = description
            val notificationManager: NotificationManager = context?.let {
                ContextCompat.getSystemService(
                    it,
                    NotificationManager::class.java
                )
            } as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }
    }
}