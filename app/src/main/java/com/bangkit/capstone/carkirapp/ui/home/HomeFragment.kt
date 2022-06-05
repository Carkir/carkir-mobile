package com.bangkit.capstone.carkirapp.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bangkit.capstone.carkirapp.R
import com.bangkit.capstone.carkirapp.data.Resource
import com.bangkit.capstone.carkirapp.data.remote.response.PlacesResponseItem
import com.bangkit.capstone.carkirapp.databinding.FragmentHomeBinding
import com.bangkit.capstone.carkirapp.model.ViewModelFactory
import com.bangkit.capstone.carkirapp.ui.adapter.PlacesAdapter
import com.bangkit.capstone.carkirapp.ui.adapter.RecentAdapter

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HomeFragment : Fragment() {

    // Declaration for placesAdapter
    private lateinit var placesAdapter: PlacesAdapter

    // Declaration for binding view
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // View Model initialization using delegate by viewModels
    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(
            requireContext(),
            requireContext().dataStore
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewModel.requestToken()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // HIDE Support Action bar on Home, Favorite and History navigation
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        // Declare Adapter for RecyclerView place and recent
        placesAdapter = PlacesAdapter()
        val recentAdapter = RecentAdapter()

        // Load token from local data store
        // This token for call private API in the server
        homeViewModel.loadTokenFromDataStore().observe(viewLifecycleOwner) { token ->

            // Load data from the server to get list of parking places
            // Response will be checking, if not null, data will be execute it based on the state
            homeViewModel.getAllParkingPlaces(token).observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    when (response) {
                        is Resource.Loading -> onLoading()
                        is Resource.Success -> showResult(response.data)
                        is Resource.Error -> onError()
                    }
                }
            }
        }

        // Load data from the local database to get recent history parking place
        // Data will be checking, if empty, show the info and if not, show text info
        homeViewModel.getRecentParkingPlaces().observe(viewLifecycleOwner) { data ->
            if (data.isNotEmpty()) {
                recentAdapter.submitList(data)
                binding.ivEmptyRecent.isVisible = false
                binding.tvEmptyRecent.isVisible = false
            } else { // On empty data
                binding.ivEmptyRecent.isVisible = true
                binding.tvEmptyRecent.isVisible = true
            }
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
        showInfo(isProgressBarShow = false, isImageShow = false, isMessageShow = false)
        placesAdapter.submitList(data)
    }

    /**
     * Handle state when response is Loading.
     * Show text info with the message
     * */
    private fun onLoading() {
        binding.tvMessageUnexpected.text = getString(R.string.carkir_home_info_loading)
        showInfo(isProgressBarShow = true, isImageShow = false)
    }

    /**
     * Handle state when response is Error.
     * Show text info with the message
     * */
    private fun onError() {
        binding.tvMessageUnexpected.text = getString(R.string.carkir_home_info_error)
        showInfo(isProgressBarShow = false, isImageShow = true)
    }

    /**
     * Show image, progress bar and text info
     * for state loading or error
     * */
    private fun showInfo(
        isProgressBarShow: Boolean,
        isImageShow: Boolean,
        isMessageShow: Boolean = true
    ) {
        binding.progressBar.isVisible = isProgressBarShow
        binding.ivErrorList.isVisible = isImageShow
        binding.tvMessageUnexpected.isVisible = isMessageShow
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