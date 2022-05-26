package com.bangkit.capstone.carkirapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.capstone.carkirapp.data.local.entity.FavoriteEntity
import com.bangkit.capstone.carkirapp.databinding.CardItemFavoriteBinding
import com.bangkit.capstone.carkirapp.ui.favorite.FavoriteFragmentDirections

class FavoriteAdapter(val onItemClicked: (FavoriteEntity) -> Unit) :
    ListAdapter<FavoriteEntity, FavoriteAdapter.FavoriteViewHolder>(mDiffCallback) {

    // Inflate the layout for items
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardItemFavoriteBinding.inflate(layoutInflater, parent, false)
        return FavoriteViewHolder(binding)
    }

    // Access the item on List
    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favoritePlace = getItem(position)
        holder.bind(favoritePlace)
        holder.binding.ivIconItemFavorite.setOnClickListener { onItemClicked(favoritePlace) }
    }

    class FavoriteViewHolder(val binding: CardItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favoritePlace: FavoriteEntity) {
            binding.apply {
                tvTimeItemFavorite.text = favoritePlace.time
                tvNameItemFavorite.text = favoritePlace.name
                tvSpaceItemFavorite.text = favoritePlace.totalSpace.toString()
            }

            // Listener to detail parking place with args name place
            itemView.setOnClickListener {
                val action = FavoriteFragmentDirections.actionNavigationFavoriteToDetailFragment(
                    favoritePlace.name
                )
                it.findNavController().navigate(action)
            }
        }

    }

    /**
     * Anonymous class for compare the oldList and newList in ListAdapter
     * */
    companion object {
        private val mDiffCallback = object : DiffUtil.ItemCallback<FavoriteEntity>() {
            override fun areItemsTheSame(
                oldItem: FavoriteEntity,
                newItem: FavoriteEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: FavoriteEntity,
                newItem: FavoriteEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}