package com.example.routines.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.routines.R
import com.example.routines.databinding.ScenarioItemBinding

class ScenarioAdapter(private val listener: Listener) : RecyclerView.Adapter<ScenarioAdapter.ScenarioHolder>() {
    private val itemScenarioList = ArrayList<ItemScenario>()

    class ScenarioHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = ScenarioItemBinding.bind(item)

        fun bind(itemScenario: ItemScenario, listener: Listener) = with(binding) {
            textView17.text = itemScenario.tagScen

            switch7.setOnCheckedChangeListener { _, isChecked ->
                val updatedItemScenario = itemScenario.copy(activScen = if (isChecked) 1 else 0)
                listener.onClick(updatedItemScenario)
            }

            itemView.setOnClickListener {
                listener.onClick(itemScenario)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScenarioHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.scenario_item, parent, false)
        return ScenarioHolder(view)
    }

    override fun getItemCount(): Int {
        return itemScenarioList.size
    }

    override fun onBindViewHolder(holder: ScenarioHolder, position: Int) {
        holder.bind(itemScenarioList[position], listener)
    }

    fun addScenario(itemScenario: ItemScenario) {
        itemScenarioList.add(itemScenario)
        notifyDataSetChanged()
    }

    fun updateScenarios(newList: List<ItemScenario>) {
        itemScenarioList.clear()
        itemScenarioList.addAll(newList)
        notifyDataSetChanged()
    }

    fun clear() {
        val size = itemScenarioList.size
        itemScenarioList.clear()
        notifyItemRangeRemoved(0, size)
    }

    interface Listener {
        fun onClick(itemScenario: ItemScenario)
    }
}