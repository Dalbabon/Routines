package com.example.routines

import android.nfc.Tag
import android.os.Bundle
import androidx.core.widget.doAfterTextChanged
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.asLiveData
import com.example.routines.adapter.ItemReminder
import com.example.routines.databinding.ActivityReminderBinding
import com.example.routines.db.Item
import com.example.routines.db.MainDb
import com.example.routines.fragments.EditorFragment

class ReminderActivity : AppCompatActivity() {
    lateinit var binding: ActivityReminderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReminderBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        if (intent?.hasExtra("item") == true) {
            val itemReminder = intent.getSerializableExtra("item") as? ItemReminder
            var isAct = itemReminder?.activRem
            itemReminder?.let { reminder ->
                binding.apply {
                    // Устанавливаем заголовок и описание
                    TextInputEditTextTitle.setText(reminder.tagRem)
                    TextInputEditTextDescr.setText(reminder.descriptionRem)

                    // Безопасно обрабатываем дату (формат ddMMyyyy или аналогичный)
                    if (reminder.dateRem.length >= 8) {
                        TextInputEditTextDay.setText(reminder.dateRem.substring(0, 2))
                        TextInputEditTextMon.setText(reminder.dateRem.substring(2, 4))
                        TextInputEditTextYear.setText(reminder.dateRem.substring(4, 8))
                    }

                    // Безопасно обрабатываем время (формат HHmm или аналогичный)
                    if (reminder.timeRem.length >= 4) {
                        TextInputEditTextHour.setText(reminder.timeRem.substring(0, 2))
                        TextInputEditTextMin.setText(reminder.timeRem.substring(2, 4))
                    }

                    saveButton.setOnClickListener{
                        var isDataHandledReminder = true
                        val db = MainDb.getDb(this@ReminderActivity)
                        val id = reminder.id
                        val title = binding.TextInputEditTextTitle.text.toString()
                        val description = binding.TextInputEditTextDescr.text.toString()
                        val date = binding.TextInputEditTextDay.text.toString() + binding.TextInputEditTextMon.text.toString() + binding.TextInputEditTextYear.text.toString()
                        val time = binding.TextInputEditTextHour.text.toString() + binding.TextInputEditTextMin.text.toString()
                        val rem = Item(id, title, description, date, time, 0)
                        val remAct = Item(id, title, description, date, time, 1)
                        if (isAct == 0){
                            db.getDao().getAllReminderItems().asLiveData().observe(this@ReminderActivity){
                                if(isDataHandledReminder){Thread{
                                    db.getDao().insertItem(rem)
                                }.start()}
                                isDataHandledReminder = false
                            }
                        }
                        else{
                            db.getDao().getAllReminderItems().asLiveData().observe(this@ReminderActivity){
                                if(isDataHandledReminder){Thread{
                                    db.getDao().insertItem(remAct)
                                }.start()}
                                isDataHandledReminder = false
                            }
                        }
                        finish()
                    }

                }
            }

        }
        else {
            onClickSave()
        }
        onClickBack()
    }

    private fun onClickSave(){
        binding.saveButton.setOnClickListener{
            val db = MainDb.getDb(this)
            val title = binding.TextInputEditTextTitle.text.toString()
            val description = binding.TextInputEditTextDescr.text.toString()
            val date = binding.TextInputEditTextDay.text.toString() + binding.TextInputEditTextMon.text.toString() + binding.TextInputEditTextYear.text.toString()
            val time = binding.TextInputEditTextHour.text.toString() + binding.TextInputEditTextMin.text.toString()
            val rem = Item(null, title, description, date, time, 0)
            Thread{db.getDao().insertItem(rem)}.start()
            finish()
        }
    }

    private fun onClickBack() {
        binding.buttonback.setOnClickListener {
            finish()
            Log.d("BTN_BACK", "Кнопка Назад")
        }
    }
}