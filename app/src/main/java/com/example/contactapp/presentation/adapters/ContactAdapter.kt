package com.example.contactapp.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.contactapp.data.remote.response.ContactResponse
import com.example.contactapp.databinding.ItemSwipeBinding

class ContactAdapter : ListAdapter<ContactResponse, ContactAdapter.ContactViewHolder>(ContactDiffUtil) {
    lateinit var itemTouchListener : ((ContactResponse) -> Unit)

    object ContactDiffUtil : DiffUtil.ItemCallback<ContactResponse>() {
        override fun areItemsTheSame(oldItem: ContactResponse, newItem: ContactResponse): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: ContactResponse, newItem: ContactResponse): Boolean {
            return oldItem.firstName == newItem.firstName
        }
    }

    inner class ContactViewHolder(private val binding: ItemSwipeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.itemForClick.setOnLongClickListener{
                itemTouchListener.invoke(getItem(absoluteAdapterPosition))
                return@setOnLongClickListener true
            }

            Log.d("TAG", "bind: ishladi")
            getItem(absoluteAdapterPosition).apply {
                binding.contactFirstName.text = firstName
                binding.contactLastName.text = lastName
                binding.contactNumber.text = phone
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        ContactViewHolder(ItemSwipeBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {

        Log.d("TAG", "bind: ${currentList.size}")
        holder.bind()
    }
}