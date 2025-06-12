package com.example.routines.adapter

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.routines.R
import com.example.routines.databinding.ReminderItemBinding
import com.example.routines.db.Item
import com.example.routines.db.MainDb
import com.example.routines.receivers.ReminderReceiver
import java.text.SimpleDateFormat
import java.util.*

class ReminderAdapter(
    private val context: Context,
    private val listener: Listener
) : RecyclerView.Adapter<ReminderAdapter.ReminderHolder>() {

    private val itemReminderList = ArrayList<ItemReminder>()
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    class ReminderHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = ReminderItemBinding.bind(item)

        fun bind(itemReminder: ItemReminder, listener: Listener, context: Context, alarmManager: AlarmManager) = with(binding) {
            // Format time
            val time = itemReminder.timeRem.toString().padStart(4, '0')
            textViewTime.text = time

            textViewTitle.text = itemReminder.tagRem
            switch2.isChecked = itemReminder.activRem == 1

            itemView.setOnClickListener {
                listener.onClick(itemReminder)
            }

            switch2.setOnCheckedChangeListener { _, isChecked ->
                val updatedItemReminder = itemReminder.copy(activRem = if (isChecked) 1 else 0)
                updateReminderInDb(updatedItemReminder)
                updateReminderAlarm(updatedItemReminder, context, alarmManager)
            }
        }

        private fun updateReminderInDb(itemReminder: ItemReminder) {
            val db = MainDb.getDb(itemView.context)
            Thread {
                val updatedItem = Item(
                    id = itemReminder.id,
                    tagRem = itemReminder.tagRem,
                    descriptionRem = itemReminder.descriptionRem,
                    dateRem = itemReminder.dateRem,
                    timeRem = itemReminder.timeRem,
                    activRem = itemReminder.activRem
                )
                db.getDao().updateItem(updatedItem)
            }.start()
        }

        private fun updateReminderAlarm(itemReminder: ItemReminder, context: Context, alarmManager: AlarmManager) {
            val intent = Intent(context, ReminderReceiver::class.java).apply {
                putExtra("id", itemReminder.id ?: 0)
                putExtra("title", itemReminder.tagRem)
                putExtra("message", itemReminder.descriptionRem)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                itemReminder.id ?: 0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            if (itemReminder.activRem == 1) {
                try {
                    val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
                    val dateTime = dateFormat.parse("${itemReminder.dateRem} ${itemReminder.timeRem}")

                    dateTime?.let {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            alarmManager.setExactAndAllowWhileIdle(
                                AlarmManager.RTC_WAKEUP,
                                it.time,
                                pendingIntent
                            )
                        } else {
                            alarmManager.setExact(
                                AlarmManager.RTC_WAKEUP,
                                it.time,
                                pendingIntent
                            )
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reminder_item, parent, false)
        return ReminderHolder(view)
    }

    override fun getItemCount(): Int = itemReminderList.size

    override fun onBindViewHolder(holder: ReminderHolder, position: Int) {
        holder.bind(itemReminderList[position], listener, context, alarmManager)
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