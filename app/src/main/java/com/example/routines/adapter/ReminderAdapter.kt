package com.example.routines.adapter

import com.example.routines.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.routines.databinding.ReminderItemBinding

class ReminderAdapter(val listener: Listener): RecyclerView.Adapter<ReminderAdapter.ReminderHolder>(){
    val itemReminderList = ArrayList<ItemReminder>()
    class ReminderHolder(item: View):RecyclerView.ViewHolder(item){
        val binding = ReminderItemBinding.bind(item)
        fun bind(itemReminder: ItemReminder, listener: Listener) = with(binding){
            textViewTime.text = itemReminder.timeRem
            textViewTitle.text = itemReminder.tagRem
            if(itemReminder.activRem == 0){
                switch2.isChecked = false
            }
            else{
                switch2.isChecked = true
            }
            itemView.setOnClickListener{
                listener.onClick(itemReminder)
            }
        }
    }

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reminder_item, parent, false)
        return ReminderHolder(view)
    }

    override fun getItemCount(): Int {
        return itemReminderList.size
    }

    override fun onBindViewHolder(holder: ReminderHolder, position: Int) {
        holder.bind(itemReminderList[position],listener)
    }

    fun addReminder(itemReminder: ItemReminder){
        itemReminderList.add(itemReminder)
        notifyDataSetChanged()
    }

    fun clear() {
        val size: Int = itemReminderList.size
        itemReminderList.clear()
        notifyItemRangeRemoved(0, size)
    }

    interface Listener{
        fun onClick(itemParts: ItemReminder)
    }
}