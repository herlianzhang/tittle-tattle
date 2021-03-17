package com.latihangoding.tittle_tattle.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.latihangoding.tittle_tattle.databinding.ItemHomeBinding
import com.latihangoding.tittle_tattle.vo.Entries

class HomeAdapter : ListAdapter<Entries, HomeAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(private val binding: ItemHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Entries) {
            binding.tvTitle.text = item.name
            binding.tvDesc.text = item.desc
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemHomeBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Entries>() {

        override fun areItemsTheSame(oldItem: Entries, newItem: Entries) =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Entries, newItem: Entries) =
            oldItem == newItem
    }
}
