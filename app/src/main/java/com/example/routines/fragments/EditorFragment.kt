package com.example.routines.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.routines.R
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

    }

    private fun switchView() {
        binding.switch1.setOnCheckedChangeListener{
                _, isChecked ->
            // isChecked - новое состояние переключателя
            if (isChecked) {
                // Действия при включении
                binding.textView7.setTextColor(Color.argb(255, 255, 177, 23))
                binding.textView6.setTextColor(Color.argb(255, 69, 77, 91))
            } else {
                // Действия при выключении

                binding.textView6.setTextColor(Color.argb(255, 255, 177, 23))
                binding.textView7.setTextColor(Color.argb(255, 69, 77, 91))
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = EditorFragment()
    }
}