package com.latihangoding.tittle_tattle.ui.contact

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.latihangoding.tittle_tattle.R
import com.latihangoding.tittle_tattle.databinding.ItemContactBinding
import com.latihangoding.tittle_tattle.vo.Contact

class ContactAdapter : ListAdapter<Contact, ContactAdapter.ViewHolder>(DiffCallback()) {

//    create view holder dari parentnya
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder.from(parent)

//    menghubungkan view holder dengan item
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(private val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {

//       menghubungkan parameter dari data di recycler view dengan parameter di item
//        atau passing data dari item ke recycler view
        fun bind(item: Contact) {
            Glide.with(binding.root).load(item.image).error(R.drawable.ic_user)
                .into(binding.ivAvatar)
            binding.tvFullname.text = item.fullname
            binding.tvPhoneNumber.text = item.phoneNumber
            binding.tvEmail.text = item.email
            binding.tvFirstname.text = item.firstname
            binding.tvLastname.text = item.lastname
        }

        companion object {
//            menghubungkan view holder dengan viewgroup yang diinginkan dengan infalte
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemContactBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact) =
            oldItem.phoneNumber == newItem.phoneNumber

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact) =
            oldItem == newItem
    }
}
