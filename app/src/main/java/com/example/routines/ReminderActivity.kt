package com.example.routines

import android.nfc.Tag
import android.os.Bundle
import androidx.core.widget.doAfterTextChanged
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.routines.databinding.ActivityReminderBinding
import com.example.routines.fragments.EditorFragment

class ReminderActivity : AppCompatActivity() {
    lateinit var binding: ActivityReminderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val minuteEditText: EditText = findViewById(R.id.editTextMinute)
//        minuteEditText.doAfterTextChanged {
//            val minutes = it.toString().toIntOrNull()
//
//            if (minutes == null || minutes !in 0..59) {
//                minuteEditText.error = "Минуты от 0 до 59"
//            } else {
//                // Всё ок — можно использовать значение
//                Log.d("TIME_INPUT", "Минуты: $minutes")
//            }
//        }

        binding = ActivityReminderBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        onClickBack()
    }


    private fun onClickBack() {
        binding.buttonBack.setOnClickListener {
            finish()
            Log.d("BTN_BACK", "Кнопка Назад")
        }
    }

}