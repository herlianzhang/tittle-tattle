package com.latihangoding.tittle_tattle.ui.timer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.latihangoding.tittle_tattle.databinding.ItemTimerBinding
import com.latihangoding.tittle_tattle.vo.Weather
import java.text.SimpleDateFormat
import java.util.*

class TimerAdapter : ListAdapter<Weather, TimerAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(private val binding: ItemTimerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Weather) {
            val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.US)
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = item.addedDate

            binding.tvDate.text = formatter.format(calendar.time)
            binding.tvDesc.text = item.desc
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemTimerBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Weather>() {

        override fun areItemsTheSame(oldItem: Weather, newItem: Weather) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Weather, newItem: Weather) =
            oldItem == newItem
    }
}