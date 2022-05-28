package com.bangkit.capstone.carkirapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.capstone.carkirapp.R
import com.bangkit.capstone.carkirapp.data.remote.response.OccupancyPlaceResponseItem
import com.bangkit.capstone.carkirapp.databinding.ItemSpaceParkingBinding

class ParkingAdapter : ListAdapter<OccupancyPlaceResponseItem, ParkingAdapter.MapParkingViewHolder>(
    mDiffCallback
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapParkingViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSpaceParkingBinding.inflate(layoutInflater, parent, false)
        return MapParkingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MapParkingViewHolder, position: Int) {
        val space = getItem(position)
        holder.bind(space)
    }

    class MapParkingViewHolder(val binding: ItemSpaceParkingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(space: OccupancyPlaceResponseItem?) {
            binding.tvCluster.text = "${space?.slot.toString()}${space?.cluster}"
            binding.cvSpace.setCardBackgroundColor(
                if (space?.occupancy == 1) {
                    itemView.resources.getColor(R.color.green)
                } else {
                    itemView.resources.getColor(R.color.red)
                }
            )
        }
    }

    /**
     * Anonymous class for compare the oldList and newList in ListAdapter
     * */
    companion object {
        private val mDiffCallback = object : DiffUtil.ItemCallback<OccupancyPlaceResponseItem>() {
            override fun areItemsTheSame(
                oldItem: OccupancyPlaceResponseItem,
                newItem: OccupancyPlaceResponseItem
            ): Boolean {
                return oldItem.slot == newItem.slot
            }

            override fun areContentsTheSame(
                oldItem: OccupancyPlaceResponseItem,
                newItem: OccupancyPlaceResponseItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}