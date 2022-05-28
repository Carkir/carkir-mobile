package com.bangkit.capstone.carkirapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.capstone.carkirapp.data.local.entity.HistoryEntity
import com.bangkit.capstone.carkirapp.databinding.CardItemRecentBinding
import com.bangkit.capstone.carkirapp.ui.home.HomeFragmentDirections

class RecentAdapter() : ListAdapter<HistoryEntity, RecentAdapter.RecentViewHolder>(mDiffCallback) {

    // Inflate the layout for items
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardItemRecentBinding.inflate(layoutInflater, parent, false)
        return RecentViewHolder(binding)
    }

    // Access the item on List
    override fun onBindViewHolder(holder: RecentViewHolder, position: Int) {
        val place = getItem(position)
        holder.bind(place)
    }

    // Binding data item to view layout
    class RecentViewHolder(val binding: CardItemRecentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(place: HistoryEntity) {
            binding.apply {
                tvNameItemRecent.text = place.name
                tvTimeItemLocation.text = place.time
                tvSpaceItemRecent.text = place.totalSpace.toString()
                ivIconItemRecent.isVisible = false
            }

            // Listener to detail parking place with args name place
            itemView.setOnClickListener {
                val action = HomeFragmentDirections.actionNavigationHomeToDetailFragment(place.name)
                it.findNavController().navigate(action)
            }
        }
    }

    /**
     * Anonymous class for compare the oldList and newList in ListAdapter
     * */
    companion object {
        private val mDiffCallback = object : DiffUtil.ItemCallback<HistoryEntity>() {
            override fun areItemsTheSame(
                oldItem: HistoryEntity,
                newItem: HistoryEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: HistoryEntity,
                newItem: HistoryEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}