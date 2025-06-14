package com.example.routines

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.routines.databinding.ActivityScenarioBinding

class ScenarioActivity : AppCompatActivity() {
    lateinit var binding: ActivityScenarioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScenarioBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        onClickBack()
        onClickReminder()
        onClickScenario()
    }

    private fun onClickReminder() {
        binding.btnIfAdd.setOnClickListener {
            startActivity(Intent(this, ScenarioIf::class.java))
        }
    }

    private fun onClickScenario() {
        binding.btnElseAdd.setOnClickListener {
            startActivity(Intent(this, ScenarioELse::class.java))
        }
    }

    private fun onClickBack(){
        binding.buttonback.setOnClickListener {
            finish()
            Log.d("Sasi","EBLAN")
        }
    }
}