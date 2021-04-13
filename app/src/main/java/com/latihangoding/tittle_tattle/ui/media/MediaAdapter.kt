package com.latihangoding.tittle_tattle.ui.media

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.latihangoding.tittle_tattle.databinding.ItemMediaBinding

class MediaAdapter : ListAdapter<Uri, MediaAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(private val binding: ItemMediaBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Uri) {

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemMediaBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
    private class DiffCallback : DiffUtil.ItemCallback<Uri>() {
        override fun areItemsTheSame(oldItem: Uri, newItem: Uri) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Uri, newItem: Uri) = oldItem == newItem
    }
}