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

    //Тут все EditText
    val hourEditText = findViewById<EditText>(R.id.editHour);
    //    val — неизменяемая (как final в Java).
    //    var — изменяемая (обычная переменная).

    //Циферки для обработчика
    var hour: Int = 0;
    var minute: Int = 0;
    var day: Int = 0;
    var month: Int = 0;
    var year: Int = 0;

    private fun checker(){

    }


    private fun test() {
        var text = hourEditText.text.toString()
        var number = hourEditText.text.toString().toInt()  // Берёт текст и конвертирует в Int
    }


    private fun onClickBack() {
        binding.buttonBack.setOnClickListener {
            finish()
            Log.d("BTN_BACK", "Кнопка Назад")
        }
    }

    private fun onClickSave() {
        binding.btnSave.setOnClickListener() {

        }
    }


}