package com.example.routines.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.routines.databinding.FragmentMainBinding
import com.example.routines.db.MainDb

class MainFragment : Fragment(), ReminderAdapter.Listener {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ReminderAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализируем адаптер с контекстом
        adapter = ReminderAdapter(requireContext(), this)

        onClickReminder()
        onClickScenario()
        setupRecyclerViewReminder()
        observeDataReminder()
    }

    override fun onResume() {
        super.onResume()
        observeDataReminder() // Загружаем данные при каждом появлении фрагмента
    }

    private fun setupRecyclerViewReminder() {
        binding.recyclerView2.layoutManager = GridLayoutManager(requireContext(), 1)
        binding.recyclerView2.adapter = adapter
    }

    private fun observeDataReminder() {
        val db = MainDb.getDb(requireContext())
        db.getDao().getActivReminderItems().asLiveData().observe(viewLifecycleOwner) { items ->
            adapter.clear() // Очищаем старые данные перед добавлением новых
            items.forEach { item ->
                val itemReminder = ItemReminder(
                    item.id,
                    item.tagRem,
                    item.descriptionRem,
                    item.dateRem,
                    item.timeRem,
                    item.activRem
                )
                adapter.addReminder(itemReminder)

                if(adapter.itemCount.equals(0))
                    binding.textView3.visibility = View.VISIBLE
                else
                    binding.textView3.visibility = View.INVISIBLE
            }
        }
    }

    private fun onClickReminder() {
        binding.imageView3.setOnClickListener {
            startActivity(Intent(requireActivity(), ReminderActivity::class.java))
        }
    }

    private fun onClickScenario() {
        binding.imageView4.setOnClickListener {
            startActivity(Intent(requireActivity(), ScenarioActivity::class.java))
        }
    }

    override fun onClick(itemReminder: ItemReminder) {
        startActivity(Intent(requireActivity(), ReminderActivity::class.java).apply {
            putExtra("item", itemReminder)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}