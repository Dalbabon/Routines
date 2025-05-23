package com.example.routines.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.routines.R
import com.example.routines.ReminderActivity
import com.example.routines.ScenarioActivity
import com.example.routines.databinding.FragmentEditorBinding


class EditorFragment : Fragment() {

    private var _binding: FragmentEditorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        switchView()
        onClickReminder()
        onClickScenario()
    }

    private fun switchView() {
        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            // isChecked - новое состояние переключателя
            if (isChecked) {
                // Действия при включении
                binding.textView6.setTextColor(Color.argb(255, 192, 183, 232))
                binding.textView7.setTextColor(Color.argb(255, 255, 255, 255))
            } else {
                // Действия при выключении

                binding.textView7.setTextColor(Color.argb(255, 192, 183, 232))
                binding.textView6.setTextColor(Color.argb(255, 255, 255, 255))
            }
        }
    }

    private fun onClickReminder() {
        binding.createReminder.setOnClickListener {
            Log.d("BTN_Reminder", "Кнопка Назад")
            startActivity(Intent(requireActivity(), ReminderActivity::class.java).apply {
            })
        }
    }
//Хуита кстати
    private fun onClickScenario() {
        binding.createScenario.setOnClickListener {
            Log.d("BTN_Scenario", "Кнопка Назад")
            startActivity(Intent(requireActivity(), ScenarioActivity::class.java).apply {
            })
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = EditorFragment()
    }
}