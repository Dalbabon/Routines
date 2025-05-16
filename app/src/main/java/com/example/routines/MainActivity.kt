package com.example.routines

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.routines.databinding.ActivityMainBinding
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

        binding.mainButton.setOnClickListener{
            supportFragmentManager.beginTransaction().replace(R.id.place_holder, MainFragment.newInstance()).commit()
            binding.mainimageView.setImageResource(R.drawable.homeoutlinedact)
            binding.editorimageView.setImageResource(R.drawable.plussquareoutlined)
            binding.settingimageView.setImageResource(R.drawable.settingoutlined)
        }

        binding.editorButton.setOnClickListener{
            supportFragmentManager.beginTransaction().replace(R.id.place_holder, EditorFragment.newInstance()).commit()
            binding.mainimageView.setImageResource(R.drawable.homeoutlined)
            binding.editorimageView.setImageResource(R.drawable.plussquareoutlinedact)
            binding.settingimageView.setImageResource(R.drawable.settingoutlined)
        }

        binding.settingButton.setOnClickListener{
            supportFragmentManager.beginTransaction().replace(R.id.place_holder, SettingFragment.newInstance()).commit()
            binding.mainimageView.setImageResource(R.drawable.homeoutlined)
            binding.editorimageView.setImageResource(R.drawable.plussquareoutlined)
            binding.settingimageView.setImageResource(R.drawable.settingoutlinedact)
        }

        supportFragmentManager.beginTransaction().replace(R.id.place_holder, MainFragment.newInstance()).commit()
        binding.mainimageView.setImageResource(R.drawable.homeoutlinedact)
        binding.editorimageView.setImageResource(R.drawable.plussquareoutlined)
        binding.settingimageView.setImageResource(R.drawable.settingoutlined)
    }
}