package com.example.routines.fragments

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.example.routines.ReminderActivity
import com.example.routines.ScenarioActivity
import com.example.routines.adapter.ItemReminder
import com.example.routines.adapter.ReminderAdapter
import com.example.routines.databinding.FragmentMainBinding
import com.example.routines.db.MainDb
import com.example.routines.receivers.ReminderReceiver
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : Fragment(), ReminderAdapter.Listener {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ReminderAdapter
    private val NOTIFICATION_PERMISSION_CODE = 1001

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkNotificationPermission()
        adapter = ReminderAdapter(requireContext(), this)

        onClickReminder()
        onClickScenario()
        setupRecyclerViewReminder()
        observeDataReminder()
    }

    override fun onResume() {
        super.onResume()
        observeDataReminder()
    }

    private fun setupRecyclerViewReminder() {
        binding.recyclerView2.layoutManager = GridLayoutManager(requireContext(), 1)
        binding.recyclerView2.adapter = adapter
    }

    private fun observeDataReminder() {
        val db = MainDb.getDb(requireContext())
        db.getDao().getActivReminderItems().asLiveData().observe(viewLifecycleOwner) { items ->
            adapter.clear()
            items.forEach { item ->
                val itemReminder = ItemReminder(
                    item.id,
                    item.tagRem,
                    item.descriptionRem,
                    item.dateRem,
                    item.timeRem,
                    item.activRem
                )
                adapter.addReminder(itemReminder)

                // Устанавливаем напоминание только если оно активно
                if (item.activRem == 1) {
                    setReminder(itemReminder)
                }
            }

            binding.textView3.visibility = if (adapter.itemCount == 0) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun setReminder(reminder: ItemReminder) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), ReminderReceiver::class.java).apply {
            putExtra("id", reminder.id ?: 0)
            putExtra("title", reminder.tagRem)
            putExtra("message", reminder.descriptionRem)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            reminder.id ?: 0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            // Парсим дату и время
            val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
            val dateTime = dateFormat.parse("${reminder.dateRem} ${reminder.timeRem}")

            dateTime?.let {
                // Проверяем, что время ещё не наступило
                if (it.time > System.currentTimeMillis()) {
                    // Устанавливаем точное время срабатывания
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        it.time,
                        pendingIntent
                    )
                    Log.d("Reminder", "Напоминание установлено на ${dateFormat.format(it)}")
                } else {
                    // Отменяем, если время уже прошло
                    alarmManager.cancel(pendingIntent)
                    Log.d("Reminder", "Время напоминания уже прошло: ${dateFormat.format(it)}")
                }
            }
        } catch (e: Exception) {
            Log.e("Reminder", "Ошибка установки напоминания", e)
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_CODE
                )
            }
        }
    }

    private fun onClickReminder() {
        binding.imageView3.setOnClickListener {
            startActivity(Intent(requireActivity(), ReminderActivity::class.java))
        }
    }

    private fun onClickScenario() {
        binding.imageView4.setOnClickListener {
            startActivity(Intent(requireActivity(), ScenarioActivity::class.java))
        }
    }

    override fun onClick(itemReminder: ItemReminder) {
        startActivity(Intent(requireActivity(), ReminderActivity::class.java).apply {
            putExtra("item", itemReminder)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}