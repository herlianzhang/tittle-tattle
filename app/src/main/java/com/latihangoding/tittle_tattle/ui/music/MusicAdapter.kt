package com.latihangoding.tittle_tattle.ui.music

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.latihangoding.tittle_tattle.R
import com.latihangoding.tittle_tattle.databinding.ItemMusicBinding
import com.latihangoding.tittle_tattle.databinding.ItemMusicGridBinding
import com.latihangoding.tittle_tattle.utils.SharedPreferenceHelper
import com.latihangoding.tittle_tattle.utils.play
import com.latihangoding.tittle_tattle.utils.reversePlay
import com.latihangoding.tittle_tattle.vo.AudioModel
import timber.log.Timber

class MusicAdapter(
    private val audioListener: AudioListener
) :
    ListAdapter<AudioModel, RecyclerView.ViewHolder>(DiffCallback()) {

    var playedIndex = -1

    fun onCompleteListener() {
        notifyItemChanged(playedIndex, true)
        playedIndex = -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (SharedPreferenceHelper(parent.context, "music").isList) {
            from(parent)
        } else {
            fromGrid(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is ViewHolder -> holder.bind(item)
            is ViewHolderGrid -> holder.bind(item)
        }

    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val payload = payloads.firstOrNull()
        if (payload != null && payload is State) {
            when (payload) {
                State.AUDIO -> {
                    when (holder) {
                        is ViewHolder -> holder.reverse()
                        is ViewHolderGrid -> holder.reverse()
                    }
                }
            }
        } else
            super.onBindViewHolder(holder, position, payloads)
    }

    inner class ViewHolder(private val binding: ItemMusicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AudioModel) {
            binding.tvTitle.text = item.title
            binding.lavPlayPause.progress = if (layoutPosition == playedIndex) 1f else 0f
            Glide.with(binding.root.context).load(item.albumUri)
                .placeholder(R.color.teal_200)
                .into(binding.civMain)

            binding.lavPlayPause.setOnClickListener {
                audioListener(item)
            }

        }

        fun reverse() {
            Timber.d("playedindex reverse position $layoutPosition")
            binding.lavPlayPause.apply {
                post {
                    reversePlay()
                }
            }
        }

        private fun audioListener(item: AudioModel) {
            binding.lavPlayPause.apply {
                Timber.d("playedIndex before = $playedIndex")
                playedIndex = if (playedIndex == layoutPosition) {
                    audioListener.stopAudio(layoutPosition)
                    reversePlay()
                    -1
                } else {
                    audioListener.startAudio(item.uri, layoutPosition)
                    play()
                    if (playedIndex != -1) {
                        audioListener.stopAudio(playedIndex)
                        notifyItemChanged(playedIndex, State.AUDIO)
                    }
                    layoutPosition
                }
                Timber.d("playedIndex after = $playedIndex")
            }
        }
    }

    inner class ViewHolderGrid(private val binding: ItemMusicGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AudioModel) {
            binding.tvTitle.text = item.title
            binding.lavPlayPause.progress = if (layoutPosition == playedIndex) 1f else 0f
            Glide.with(binding.root.context).load(item.albumUri)
                .placeholder(R.color.teal_200)
                .into(binding.civMain)

            binding.lavPlayPause.setOnClickListener {
                audioListener(item)
            }

        }

        fun reverse() {
            Timber.d("playedindex reverse position $layoutPosition")
            binding.lavPlayPause.apply {
                post {
                    reversePlay()
                }
            }
        }

        private fun audioListener(item: AudioModel) {
            binding.lavPlayPause.apply {
                Timber.d("playedIndex before = $playedIndex")
                playedIndex = if (playedIndex == layoutPosition) {
                    audioListener.stopAudio(layoutPosition)
                    reversePlay()
                    -1
                } else {
                    audioListener.startAudio(item.uri, layoutPosition)
                    play()
                    if (playedIndex != -1) {
                        audioListener.stopAudio(playedIndex)
                        notifyItemChanged(playedIndex, State.AUDIO)
                    }
                    layoutPosition
                }
                Timber.d("playedIndex after = $playedIndex")
            }
        }
    }

    private fun from(parent: ViewGroup): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMusicBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    private fun fromGrid(parent: ViewGroup): ViewHolderGrid {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMusicGridBinding.inflate(layoutInflater, parent, false)
        return ViewHolderGrid(binding)
    }

    private class DiffCallback : DiffUtil.ItemCallback<AudioModel>() {
        override fun areItemsTheSame(oldItem: AudioModel, newItem: AudioModel): Boolean =
            oldItem.uri == newItem.uri

        override fun areContentsTheSame(oldItem: AudioModel, newItem: AudioModel): Boolean =
            oldItem == newItem
    }

    interface AudioListener {
        fun startAudio(uri: Uri, position: Int)
        fun stopAudio(position: Int)
    }

    enum class State {
        AUDIO
    }
}