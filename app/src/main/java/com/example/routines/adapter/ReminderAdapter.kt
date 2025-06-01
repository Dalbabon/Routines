package com.example.routines.adapter

import android.content.Context
import android.graphics.Color
import com.example.routines.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.routines.databinding.ReminderItemBinding
import com.example.routines.db.Item
import com.example.routines.db.MainDb

class ReminderAdapter(
    private val context: Context,
    private val listener: Listener
): RecyclerView.Adapter<ReminderAdapter.ReminderHolder>() {
    private val itemReminderList = ArrayList<ItemReminder>()

    class ReminderHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = ReminderItemBinding.bind(item)

        fun bind(itemReminder: ItemReminder, listener: Listener) = with(binding) {
            // Format time
            val time = itemReminder.timeRem.toString().padStart(4, '0')
            textViewTime.text = if (time.length == 4) {
                "${time.substring(0, 2)}:${time.substring(2)}"
            } else {
                "00:00"
            }

            textViewTitle.text = itemReminder.tagRem
            switch2.isChecked = itemReminder.activRem == 1

            itemView.setOnClickListener {
                listener.onClick(itemReminder)
            }

            binding.switch2.setOnCheckedChangeListener { _, isChecked ->
                val db = MainDb.getDb(itemView.context)
                Thread {
                    // Convert ItemReminder to the database Item type
                    val updatedItem = Item(
                        id = itemReminder.id,
                        tagRem = itemReminder.tagRem,
                        descriptionRem = itemReminder.descriptionRem,
                        dateRem = itemReminder.dateRem,
                        timeRem = itemReminder.timeRem,
                        activRem = if (isChecked) 1 else 0
                    )
                    db.getDao().updateItem(updatedItem)
                }.start()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reminder_item, parent, false)
        return ReminderHolder(view)
    }

    override fun getItemCount(): Int = itemReminderList.size

    override fun onBindViewHolder(holder: ReminderHolder, position: Int) {
        holder.bind(itemReminderList[position], listener)
    }

    fun addReminder(itemReminder: ItemReminder) {
        itemReminderList.add(itemReminder)
        notifyItemInserted(itemReminderList.size - 1)
    }

    fun clear() {
        val size = itemReminderList.size
        itemReminderList.clear()
        notifyItemRangeRemoved(0, size)
    }

    interface Listener {
        fun onClick(itemReminder: ItemReminder)
    }
}