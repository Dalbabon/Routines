package com.example.routines.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.example.routines.ReminderActivity
import com.example.routines.ScenarioActivity
import com.example.routines.adapter.ItemReminder
import com.example.routines.adapter.ReminderAdapter
import com.example.routines.databinding.FragmentEditorBinding
import com.example.routines.db.MainDb

class EditorFragment : Fragment(), ReminderAdapter.Listener {

    private var _binding: FragmentEditorBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ReminderAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация адаптера с контекстом
        adapter = ReminderAdapter(requireContext(), this)

        initViews()
        setupRecyclerView()
        observeDataReminder()
    }

    override fun onResume() {
        super.onResume()
        observeDataReminder() // Загружаем данные при каждом появлении фрагмента
    }

    private fun initViews() {
        switchView()
        setupClickListeners()
    }

    private fun switchView() {
        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            val activeColor = Color.argb(255, 192, 183, 232)
            val inactiveColor = Color.argb(255, 255, 255, 255)

            binding.textView6.setTextColor(if (isChecked) activeColor else inactiveColor)
            binding.textView7.setTextColor(if (isChecked) inactiveColor else activeColor)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView4.apply {
            layoutManager = GridLayoutManager(requireContext(), 1)
            adapter = this@EditorFragment.adapter
        }
    }

    private fun observeDataReminder() {
        val db = MainDb.getDb(requireContext())
        db.getDao().getAllReminderItems().asLiveData().observe(viewLifecycleOwner) { items ->
            adapter.clear() // Очищаем предыдущие данные
            items.forEach { item ->
                adapter.addReminder(
                    ItemReminder(
                        item.id,
                        item.tagRem,
                        item.descriptionRem,
                        item.dateRem,
                        item.timeRem,
                        item.activRem
                    )
                )
                if(adapter.itemCount.equals(0))
                    binding.textView5.visibility = View.VISIBLE
                else
                    binding.textView5.visibility = View.INVISIBLE
            }
        }
    }

    private fun setupClickListeners() {
        binding.createReminder.setOnClickListener {
            startActivity(Intent(requireActivity(), ReminderActivity::class.java))
        }

        binding.createScenario.setOnClickListener {
            startActivity(Intent(requireActivity(), ScenarioActivity::class.java))
        }
    }

    override fun onClick(itemReminder: ItemReminder) {
        startActivity(
            Intent(requireActivity(), ReminderActivity::class.java).apply {
                putExtra("item", itemReminder)
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = EditorFragment()
    }
}