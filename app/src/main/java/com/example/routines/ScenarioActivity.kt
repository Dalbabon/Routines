package com.example.routines

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
        //onClickBack()
    }

//    private fun onClickBack(){
//        binding.buttonBackSC.setOnClickListener {
//            finish()
//            Log.d("Sasi","EBLAN")
//        }
//    }
}