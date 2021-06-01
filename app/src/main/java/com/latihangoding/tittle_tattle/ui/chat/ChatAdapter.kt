package com.latihangoding.tittle_tattle.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.latihangoding.tittle_tattle.R
import com.latihangoding.tittle_tattle.databinding.ItemChatReceiverBinding
import com.latihangoding.tittle_tattle.databinding.ItemChatSenderBinding
import com.latihangoding.tittle_tattle.databinding.ItemChatSeparatorBinding
import com.latihangoding.tittle_tattle.vo.Chat
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter : ListAdapter<ItemData, RecyclerView.ViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            SENDER -> SenderViewHolder.from(parent)
            RECEIVER -> ReceiverViewHolder.from(parent)
            else -> SeparatorViewHolder.from(parent)
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is SenderViewHolder -> holder.bind((item as ItemData.Content).chat)
            is ReceiverViewHolder -> holder.bind((item as ItemData.Content).chat)
            is SeparatorViewHolder -> holder.bind((item as ItemData.Separator).date)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when {
            item is ItemData.Content && item.chat.senderId == Firebase.auth.uid -> SENDER
            item is ItemData.Content && item.chat.senderId != Firebase.auth.uid -> RECEIVER
            else -> SEPARATOR
        }
    }


    class ReceiverViewHolder(private val binding: ItemChatReceiverBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Chat) {
            val date = Date(item.timestamp?.toLongOrNull() ?: 0L)
            val sdf = SimpleDateFormat("hh:mm", Locale.US)

            binding.tvMessage.text = item.messageText
            binding.tvTimestamp.text = sdf.format(date)
        }

        companion object {
            fun from(parent: ViewGroup): ReceiverViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemChatReceiverBinding.inflate(layoutInflater, parent, false)
                return ReceiverViewHolder(binding)
            }
        }
    }

    class SenderViewHolder(private val binding: ItemChatSenderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Chat) {
            val date = Date(item.timestamp?.toLongOrNull() ?: 0L)
            val sdf = SimpleDateFormat("HH:mm", Locale.US)

            Glide.with(binding.root)
                .load(if (item.isPending) R.drawable.ic_pending else R.drawable.ic_check)
                .into(binding.ivStatus)

            binding.tvMessage.text = item.messageText
            binding.tvTimestamp.text = sdf.format(date)
        }

        companion object {
            fun from(parent: ViewGroup): SenderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemChatSenderBinding.inflate(layoutInflater, parent, false)
                return SenderViewHolder(binding)
            }
        }
    }

    class SeparatorViewHolder(private val binding: ItemChatSeparatorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            binding.tvMain.text = item
        }

        companion object {
            fun from(parent: ViewGroup): SeparatorViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemChatSeparatorBinding.inflate(layoutInflater, parent, false)
                return SeparatorViewHolder(binding)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<ItemData>() {
        override fun areItemsTheSame(oldItem: ItemData, newItem: ItemData): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ItemData, newItem: ItemData): Boolean =
            oldItem == newItem
    }

    companion object {
        private const val SENDER = 0
        private const val RECEIVER = 1
        private const val SEPARATOR = 2
    }
}

sealed class ItemData {
    data class Content(val chat: Chat) : ItemData() {
        override val id: Long = chat.timestamp?.toLongOrNull() ?: 0L
    }

    data class Separator(val date: String, val datePosition: Long) : ItemData() {
        override val id: Long = datePosition
    }

    abstract val id: Long
}