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
import com.bangkit.capstone.carkirapp.ui.home.HomeFragmentDirections
import com.bangkit.capstone.carkirapp.utils.decodeBase64ToBitmap
import com.bangkit.capstone.carkirapp.utils.loadImage

class RecentAdapter : ListAdapter<PlacesEntity, RecentAdapter.RecentViewHolder>(mDiffCallback) {

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
        fun bind(place: PlacesEntity) {
            binding.apply {
                place.image?.let {
                    val bitmap = it.decodeBase64ToBitmap()
                    ivThumbnailItemRecent.loadImage(itemView.context, bitmap)
                }
                tvNameItemRecent.text = place.name
                tvTimeItemLocation.text = itemView.resources.getString(
                    R.string.carkir_card_see_at,
                    place.insertAt
                )
                tvSpaceItemRecent.text = itemView.resources.getString(
                    R.string.carkir_card_slot_parking,
                    place.totalSpace
                )
                ivIconItemRecent.isVisible = false
            }

            // Listener to detail parking place with args name place
            itemView.setOnClickListener {
                val action =
                    HomeFragmentDirections.actionNavigationHomeToDetailPlaceActivity(place.name)
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