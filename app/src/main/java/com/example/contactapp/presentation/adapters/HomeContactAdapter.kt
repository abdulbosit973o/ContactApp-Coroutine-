package com.example.contactapp.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.contactapp.data.model.ContactUIData
import com.example.contactapp.data.model.StatusEnum
import com.example.contactapp.databinding.ItemSwipeBinding

class HomeContactAdapter : ListAdapter<ContactUIData, HomeContactAdapter.HomeViewHolder>(ContactDiffUtil) {
    lateinit var itemTouchListener : ((ContactUIData) -> Unit)
    lateinit var itemLongTouchListener : ((ContactUIData) -> Unit)

    object ContactDiffUtil : DiffUtil.ItemCallback<ContactUIData>() {
        override fun areItemsTheSame(oldItem: ContactUIData, newItem: ContactUIData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ContactUIData, newItem: ContactUIData): Boolean {
            return oldItem == newItem
        }
    }

    inner class HomeViewHolder(binding: ItemSwipeBinding) : ViewHolder(binding.root){
        private val firstname = binding.contactFirstName
        private val lastname = binding.contactLastName
        private val phone = binding.contactNumber
        private val click = binding.itemForClick
        private val status = binding.textStatus

        fun bind() {
            click.setOnClickListener {
                itemTouchListener.invoke(getItem(absoluteAdapterPosition))
            }
            when(getItem(absoluteAdapterPosition).status) {
                StatusEnum.SYNC -> status.visibility = View.GONE
                else  -> {
                    status.visibility = View.VISIBLE
                    status.text = getItem(absoluteAdapterPosition).status.name
                }
            }
            click.setOnLongClickListener {
                itemLongTouchListener.invoke(getItem(absoluteAdapterPosition))
                return@setOnLongClickListener true
            }
            firstname.text = getItem(absoluteAdapterPosition).firstName
            lastname.text = getItem(absoluteAdapterPosition).lastName
            phone.text = getItem(absoluteAdapterPosition).phone
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder =
        HomeViewHolder(ItemSwipeBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind()
    }
}