package com.latihangoding.tittle_tattle.ui.room_chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.latihangoding.tittle_tattle.databinding.ItemRoomChatBinding
import com.latihangoding.tittle_tattle.vo.User

class RoomChatAdapter(private val onClickListener: OnClickListener) : ListAdapter<User, RoomChatAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

        holder.itemView.setOnClickListener {
            onClickListener.onClick(item)
        }
    }

    class ViewHolder private constructor(private val binding: ItemRoomChatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: User) {
            Glide.with(binding.root).load(item.photoUrl).into(binding.ivAvatar)
            binding.tvName.text = item.name
            binding.tvEmail.text = item.email
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemRoomChatBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<User>() {

        override fun areItemsTheSame(oldItem: User, newItem: User) = oldItem.uid == newItem.uid
        override fun areContentsTheSame(oldItem: User, newItem: User) = oldItem == newItem
    }

    interface OnClickListener {
        fun onClick(user: User)
    }
}