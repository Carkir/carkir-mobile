package com.bangkit.capstone.carkirapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.capstone.carkirapp.R
import com.bangkit.capstone.carkirapp.data.local.entity.PlacesEntity
import com.bangkit.capstone.carkirapp.databinding.CardItemRecentBinding
import com.bangkit.capstone.carkirapp.ui.history.HistoryFragmentDirections

class HistoryAdapter(val onItemClicked: (PlacesEntity) -> Unit) :
    ListAdapter<PlacesEntity, HistoryAdapter.HistoryViewHolder>(mDiffCallback) {

    // Inflate the layout for items
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardItemRecentBinding.inflate(layoutInflater, parent, false)
        return HistoryViewHolder(binding)
    }

    // Access the item on List
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val place = getItem(position)
        holder.bind(place)
        holder.binding.ivIconItemRecent.setOnClickListener { onItemClicked(place) }
    }

    // Binding data item to view layout
    class HistoryViewHolder(val binding: CardItemRecentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(place: PlacesEntity) {
            binding.apply {
                tvNameItemRecent.text = place.name
                tvTimeItemLocation.text = itemView.resources.getString(
                    R.string.carkir_card_see_at,
                    place.insertAt
                )
                tvSpaceItemRecent.text = itemView.resources.getString(
                    R.string.carkir_card_slot_parking,
                    place.totalSpace
                )
                ivIconItemRecent.isVisible = true
            }

            // Listener to detail parking place with args name place
            itemView.setOnClickListener {
                val action =
                    HistoryFragmentDirections.actionNavigationHistoryToDetailPlaceActivity(place.name)
                it.findNavController().navigate(action)
            }
        }

    }

    /**
     * Anonymous class for compare the oldList and newList in ListAdapter
     * */
    companion object {
        private val mDiffCallback = object : DiffUtil.ItemCallback<PlacesEntity>() {
            override fun areItemsTheSame(
                oldItem: PlacesEntity,
                newItem: PlacesEntity
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: PlacesEntity,
                newItem: PlacesEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}