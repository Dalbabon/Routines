package com.example.routines

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.routines.adapter.ItemReminder
import com.example.routines.databinding.ActivityMainBinding
import com.example.routines.db.MainDb
import com.example.routines.fragments.EditorFragment
import com.example.routines.fragments.MainFragment
import com.example.routines.fragments.SettingFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        createNotificationChannel()
        handleNotificationIntent(intent)

// Я НЕНАВИЖУ ФРАГМЕНТЫ!
        binding.mainButton.setOnClickListener{
            supportFragmentManager.beginTransaction().replace(R.id.place_holder, MainFragment.newInstance()).commit()
            binding.mainimageView.setImageResource(R.drawable.homeoutlinedact)
            binding.editorimageView.setImageResource(R.drawable.plusoutlined)
            binding.settingimageView.setImageResource(R.drawable.settingoutlined)
        }

        binding.editorButton.setOnClickListener{
            supportFragmentManager.beginTransaction().replace(R.id.place_holder, EditorFragment.newInstance()).commit()
            binding.mainimageView.setImageResource(R.drawable.homeoutlined)
            binding.editorimageView.setImageResource(R.drawable.plusoutlinedact)
            binding.settingimageView.setImageResource(R.drawable.settingoutlined)
        }

        binding.settingButton.setOnClickListener{
            supportFragmentManager.beginTransaction().replace(R.id.place_holder, SettingFragment.newInstance()).commit()
            binding.mainimageView.setImageResource(R.drawable.homeoutlined)
            binding.editorimageView.setImageResource(R.drawable.plusoutlined)
            binding.settingimageView.setImageResource(R.drawable.settingoutlinedact)
        }

        supportFragmentManager.beginTransaction().replace(R.id.place_holder, MainFragment.newInstance()).commit()
        binding.mainimageView.setImageResource(R.drawable.homeoutlinedact)
        binding.editorimageView.setImageResource(R.drawable.plusoutlined)
        binding.settingimageView.setImageResource(R.drawable.settingoutlined)
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "reminder_channel",
                "Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for reminder notifications"
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleNotificationIntent(intent)
    }

    private fun handleNotificationIntent(intent: Intent?) {
        val reminderId = intent?.getIntExtra("reminder_id", -1) ?: -1
        if (reminderId != -1) {
            // Открываем фрагмент с деталями напоминания
            openReminderDetails(reminderId)
        }
    }

    private fun openReminderDetails(reminderId: Int) {
        val db = MainDb.getDb(this)
        Thread {
            val item = db.getDao().getReminderById(reminderId)
            runOnUiThread {
                if (item != null) {
                    val reminder = ItemReminder(
                        item.id,
                        item.tagRem,
                        item.descriptionRem,
                        item.dateRem,
                        item.timeRem,
                        item.activRem
                    )
                    // Открываем экран редактирования напоминания
                    startActivity(Intent(this, ReminderActivity::class.java).apply {
                        putExtra("item", reminder)
                    })
                }
            }
        }.start()
    }
}
