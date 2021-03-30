package com.mahmoudbashir.todoapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mahmoudbashir.todoapp.R
import com.mahmoudbashir.todoapp.model.DataModel

public class ToDoList_adapter(val context:Context): RecyclerView.Adapter<ToDoList_adapter.ViewHolder>() {

    inner class ViewHolder(item : View) : RecyclerView.ViewHolder(item){
        val title: TextView = item.findViewById<TextView>(R.id.txt_title)
        val date: TextView = item.findViewById<TextView>(R.id.txt_date)
        val desc: TextView = item.findViewById<TextView>(R.id.txt_desc)
    }

    private val differCallback = object : DiffUtil.ItemCallback<DataModel>(){
        override fun areItemsTheSame(oldItem: DataModel, newItem: DataModel): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: DataModel, newItem: DataModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.single_item_list,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.title.text = differ.currentList[position].title
       holder.date.text = differ.currentList[position].date
       holder.desc.text = differ.currentList[position].description
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}