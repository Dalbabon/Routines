package com.example.routines

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import com.example.routines.adapter.ItemReminder
import com.example.routines.databinding.ActivityReminderBinding
import com.example.routines.db.Item
import com.example.routines.db.MainDb
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ReminderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReminderBinding
    private var existingReminder: ItemReminder? = null

    // Регулярные выражения для валидации
    private companion object {
        val DAY_PATTERN = Regex("^(0[1-9]|[12][0-9]|3[01])$")     // 01-31
        val MONTH_PATTERN = Regex("^(0[1-9]|1[0-2])$")            // 01-12
        val YEAR_PATTERN = Regex("^(19|20)\\d{2}$")               // 1900-2099
        val HOUR_PATTERN = Regex("^([01][0-9]|2[0-3])$")          // 00-23
        val MINUTE_PATTERN = Regex("^[0-5][0-9]$")                // 00-59
    }

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

        val reminder = intent.getSerializableExtra("item") as? ItemReminder
        if (reminder != null) {
            showReminderDetails(reminder)
        } else {
            setupNewReminder()
        }

        setupInputValidation()
        setupClickListeners()
    }

    private fun showReminderDetails(reminder: ItemReminder) {
        existingReminder = reminder
        binding.apply {
            TextInputEditTextTitle.setText(reminder.tagRem)
            TextInputEditTextDescr.setText(reminder.descriptionRem)

            if (reminder.dateRem.length >= 10) {
                TextInputEditTextDay.setText(reminder.dateRem.substring(0, 2))
                TextInputEditTextMon.setText(reminder.dateRem.substring(3, 5))
                TextInputEditTextYear.setText(reminder.dateRem.substring(6, 10))
            }

            if (reminder.timeRem.length >= 5) {
                TextInputEditTextHour.setText(reminder.timeRem.substring(0, 2))
                TextInputEditTextMin.setText(reminder.timeRem.substring(3, 5))
            }
        }
    }

    private fun setupNewReminder() {
        // Очищаем поля для нового напоминания
        binding.apply {
            TextInputEditTextTitle.text?.clear()
            TextInputEditTextDescr.text?.clear()
            TextInputEditTextDay.text?.clear()
            TextInputEditTextMon.text?.clear()
            TextInputEditTextYear.text?.clear()
            TextInputEditTextHour.text?.clear()
            TextInputEditTextMin.text?.clear()
        }
        existingReminder = null
    }

    private fun setupInputValidation() {
        binding.apply {
            TextInputEditTextDay.doAfterTextChanged { text ->
                validateField(text, DAY_PATTERN, TextInputLayoutDay, "День (01-31)")
            }

            TextInputEditTextMon.doAfterTextChanged { text ->
                validateField(text, MONTH_PATTERN, TextInputLayoutMon, "Месяц (01-12)")
            }

            TextInputEditTextYear.doAfterTextChanged { text ->
                validateField(text, YEAR_PATTERN, TextInputLayoutYear, "Год (1900-2099)")
            }

            TextInputEditTextHour.doAfterTextChanged { text ->
                validateField(text, HOUR_PATTERN, TextInputLayoutHour, "Часы (00-23)")
            }

            TextInputEditTextMin.doAfterTextChanged { text ->
                validateField(text, MINUTE_PATTERN, TextInputLayoutMin, "Минуты (00-59)")
            }
        }
    }

    private fun validateField(text: CharSequence?, pattern: Regex, layout: TextInputLayout, errorMsg: String): Boolean {
        return if (!text.isNullOrEmpty() && !pattern.matches(text)) {
            layout.error = "Некорректный $errorMsg"
            false
        } else {
            layout.error = null
            true
        }
    }

    private fun setupClickListeners() {
        binding.saveButton.setOnClickListener {
            if (validateAllInputs()) {
                saveReminder()
            } else {
                Toast.makeText(this, "Проверьте правильность введенных данных", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonback.setOnClickListener {
            finish()
            Log.d("BTN_BACK", "Кнопка Назад")
        }
    }

    private fun validateAllInputs(): Boolean {
        var isValid = true

        isValid = isValid && validateField(binding.TextInputEditTextDay.text, DAY_PATTERN, binding.TextInputLayoutDay, "День")
        isValid = isValid && validateField(binding.TextInputEditTextMon.text, MONTH_PATTERN, binding.TextInputLayoutMon, "Месяц")
        isValid = isValid && validateField(binding.TextInputEditTextYear.text, YEAR_PATTERN, binding.TextInputLayoutYear, "Год")
        isValid = isValid && validateField(binding.TextInputEditTextHour.text, HOUR_PATTERN, binding.TextInputLayoutHour, "Часы")
        isValid = isValid && validateField(binding.TextInputEditTextMin.text, MINUTE_PATTERN, binding.TextInputLayoutMin, "Минуты")
        isValid = isValid && binding.TextInputEditTextTitle.text.toString().isNotBlank()

        return isValid
    }

    private fun saveReminder() {
        val db = MainDb.getDb(this)
        val title = binding.TextInputEditTextTitle.text.toString()
        val description = binding.TextInputEditTextDescr.text.toString()
        val date = "${binding.TextInputEditTextDay.text}-${binding.TextInputEditTextMon.text}-${binding.TextInputEditTextYear.text}"
        val time = "${binding.TextInputEditTextHour.text}:${binding.TextInputEditTextMin.text}"
        val isActive = if (existingReminder != null) existingReminder!!.activRem else 1 // По умолчанию активно

        val item = Item(
            id = existingReminder?.id,
            tagRem = title,
            descriptionRem = description,
            dateRem = date,
            timeRem = time,
            activRem = isActive
        )

        GlobalScope.launch(Dispatchers.IO) {
            try {
                if (existingReminder == null) {
                    db.getDao().insertItem(item)
                } else {
                    db.getDao().updateItem(item)
                }
                runOnUiThread {
                    Toast.makeText(this@ReminderActivity, "Напоминание сохранено", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@ReminderActivity, "Ошибка сохранения: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Очистка ресурсов при необходимости
    }
}