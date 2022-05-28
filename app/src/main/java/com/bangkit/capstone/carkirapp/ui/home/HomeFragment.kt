package com.bangkit.capstone.carkirapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bangkit.capstone.carkirapp.R
import com.bangkit.capstone.carkirapp.data.Resource
import com.bangkit.capstone.carkirapp.data.remote.response.PlacesResponseItem
import com.bangkit.capstone.carkirapp.databinding.FragmentHomeBinding
import com.bangkit.capstone.carkirapp.model.ViewModelFactory
import com.bangkit.capstone.carkirapp.ui.adapter.PlacesAdapter
import com.bangkit.capstone.carkirapp.ui.adapter.RecentAdapter

// TODO TO HIDE ACTION BAR
class HomeFragment : Fragment() {

    // Declaration for placesAdapter
    private lateinit var placesAdapter: PlacesAdapter

    // Declaration for binding view
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // View Model initialization using delegate by viewModels
    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Declare Adapter for RecyclerView place and recent
        placesAdapter = PlacesAdapter()
        val recentAdapter = RecentAdapter()

        // Load data from the server to get list of parking places
        // Response will be checking, if not null, data will be execute it based on the state
        homeViewModel.getAllParkingPlaces().observe(viewLifecycleOwner) { response ->
            if (response != null) {
                when (response) {
                    is Resource.Loading -> onLoading()
                    is Resource.Success -> showResult(response.data)
                    is Resource.Error -> onError()
                }
            }
        }

        // Load data from the local database to get recent history parking place
        // Data will be checking, if empty, show the info and if not, show text info
        homeViewModel.getRecentParkingPlaces().observe(viewLifecycleOwner) { data ->
            if (data.isNotEmpty()) {
                recentAdapter.submitList(data)
                binding.tvEmptyRecent.isVisible = false
            } else {
                binding.tvEmptyRecent.isVisible = true // On empty data
            }
        }

        // Move to the tab History
        // TODO: FIX BUG CANNOT MOVE TO OTHER TAB AFTER RUN THIS LISTENER
        binding.containerTitleRecent.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_navigation_history)
        }

        // Setup the adapter of Recycler View
        binding.rvLocation.adapter = placesAdapter
        binding.rvRecent.adapter = recentAdapter
    }

    /**
     * Handle state when response is Success
     * Hide the text info and put the data to the adapter
     * */
    private fun showResult(data: List<PlacesResponseItem>?) {
        binding.containerUnexpected.isVisible = false
        placesAdapter.submitList(data)
    }

    /**
     * Handle state when response is Loading.
     * Show text info with the message
     * */
    private fun onLoading() {
        binding.containerUnexpected.isVisible = true
        binding.tvMessageUnexpected.text = getString(R.string.carkir_home_info_loading)
    }

    /**
     * Handle state when response is Error.
     * Show text info with the message
     * */
    private fun onError() {
        binding.containerUnexpected.isVisible = true
        binding.tvMessageUnexpected.text = getString(R.string.carkir_home_info_error)
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