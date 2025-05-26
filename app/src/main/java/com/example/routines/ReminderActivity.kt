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



        //val item = intent.getSerializableExtra("item") as ItemReminder
        //var isDataHadledRemind = true
//        binding.apply {
//            TextInputEditTextTitle.setText(item.tagRem)
//            TextInputEditTextDescr.setText(item.descriptionRem)
//            TextInputEditTextDay.setText(item.dateRem.substring(0, 2))
//            TextInputEditTextMon.setText(item.dateRem.substring(3, 5))
//            TextInputEditTextYear.setText(item.dateRem.substring(6, 11))
//            TextInputEditTextHour.setText(item.timeRem.substring(0, 2))
//            TextInputEditTextMin.setText(item.timeRem.substring(3, 5))
//        }
        onClickBack()
        onClickSave()
    }

    private fun onClickSave(){
        binding.saveButton.setOnClickListener{
            val db = MainDb.getDb(this)
            val title = binding.TextInputEditTextTitle.text.toString()
            val description = binding.TextInputEditTextDescr.text.toString()
            val date = binding.TextInputEditTextDay.text.toString() + binding.TextInputEditTextMon.text.toString() + binding.TextInputEditTextYear.text.toString()
            val time = binding.TextInputEditTextHour.text.toString() + binding.TextInputEditTextMin.text.toString()
            val rem = Item(null, title, description, date, time, 0)
            //db.getDao().getAllItems().asLiveData().observe(this){
                //if (it.isNullOrEmpty()) {
                    Thread{db.getDao().insertItem(rem)}.start()
                //}
            //}
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