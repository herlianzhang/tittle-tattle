package com.latihangoding.tittle_tattle.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.latihangoding.tittle_tattle.databinding.ItemGalleryBinding

import com.latihangoding.tittle_tattle.vo.GalleryModel

class GalleryAdapter : ListAdapter<GalleryModel, GalleryAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


    class ViewHolder private constructor(private val binding: ItemGalleryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GalleryModel) {
            Glide.with(binding.root).load(item.imgPath).into(binding.sivMain)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemGalleryBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<GalleryModel>() {

        override fun areItemsTheSame(oldItem: GalleryModel, newItem: GalleryModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: GalleryModel, newItem: GalleryModel) =
            oldItem == newItem
    }
}
