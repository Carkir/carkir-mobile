package com.bangkit.capstone.carkirapp.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bangkit.capstone.carkirapp.databinding.FragmentFavoriteBinding
import com.bangkit.capstone.carkirapp.model.ViewModelFactory
import com.bangkit.capstone.carkirapp.ui.adapter.FavoriteAdapter

// TODO TO HIDE ACTION BAR
class FavoriteFragment : Fragment() {

    // Declaration for binding view
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    // View Model initialization using delegate by viewModels
    private val favoriteViewModel: FavoriteViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Declaration adapter for recycler view favorite parking place
        // The lambda function is use for remove place
        val favoriteAdapter = FavoriteAdapter { place ->
            favoriteViewModel.deleteFavoritePlace(place)
        }

        // Load data from the local database to get list favorite parking place
        // Data will be checking, if empty show the info and if not show list of parking place
        favoriteViewModel.loadAllFavoritePlaces().observe(viewLifecycleOwner) { places ->
            if (places.isNotEmpty()) {
                favoriteAdapter.submitList(places)
                binding.emptyFavorite.isVisible = false
            } else {
                binding.emptyFavorite.isVisible = true
            }
        }

        // Set adapter to recycler view
        binding.rvHistory.adapter = favoriteAdapter
    }

    /**
     * Set _binding to null when fragment is close
     * to avoid memory-leak.
     * */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}