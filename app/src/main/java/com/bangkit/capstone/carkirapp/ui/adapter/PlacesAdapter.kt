package com.bangkit.capstone.carkirapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.capstone.carkirapp.data.remote.response.PlacesResponseItem
import com.bangkit.capstone.carkirapp.databinding.CardItemLocationBinding
import com.bangkit.capstone.carkirapp.ui.home.HomeFragmentDirections

class PlacesAdapter() :
    ListAdapter<PlacesResponseItem, PlacesAdapter.PlacesViewHolder>(mDiffCallback) {

    // Inflate the layout for items
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardItemLocationBinding.inflate(layoutInflater, parent, false)
        return PlacesViewHolder(binding)
    }

    // Access the item on List
    override fun onBindViewHolder(holder: PlacesViewHolder, position: Int) {
        val place = getItem(position)
        holder.bind(place)
    }

    // Binding data item to view layout
    class PlacesViewHolder(val binding: CardItemLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(place: PlacesResponseItem) {
            val (name, time, totalEmptySpace, status) = place
            binding.apply {
                tvNameItemLocation.text = name
                tvStatusItemLocation.text = status
                tvTimeItemLocation.text = time
                tvSpaceItemLocation.text = totalEmptySpace.toString()
            }

            // Listener to detail parking place with args name place
            itemView.setOnClickListener {
                val action = HomeFragmentDirections.actionNavigationHomeToDetailFragment(name)
                it.findNavController().navigate(action)
            }
        }
    }

    /**
     * Anonymous class for compare the oldList and newList in ListAdapter
     * */
    companion object {
        private val mDiffCallback = object : DiffUtil.ItemCallback<PlacesResponseItem>() {
            override fun areItemsTheSame(
                oldItem: PlacesResponseItem,
                newItem: PlacesResponseItem
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: PlacesResponseItem,
                newItem: PlacesResponseItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}