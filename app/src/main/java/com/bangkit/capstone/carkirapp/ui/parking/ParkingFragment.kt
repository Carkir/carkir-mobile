package com.bangkit.capstone.carkirapp.ui.parking

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.bangkit.capstone.carkirapp.R
import com.bangkit.capstone.carkirapp.data.Resource
import com.bangkit.capstone.carkirapp.data.remote.response.OccupancyPlaceResponseItem
import com.bangkit.capstone.carkirapp.databinding.FragmentParkingBinding
import com.bangkit.capstone.carkirapp.model.ViewModelFactory
import com.bangkit.capstone.carkirapp.ui.adapter.ParkingAdapter

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ParkingFragment : Fragment() {

    // Declaration for adapter
    private lateinit var parkingAdapter: ParkingAdapter

    // Declaration for binding view
    private var _binding: FragmentParkingBinding? = null
    private val binding get() = _binding!!

    // View Model initialization using delegate by viewModels
    private val parkingViewModel: ParkingViewModel by viewModels {
        ViewModelFactory.getInstance(
            requireContext(),
            requireContext().dataStore
        )
    }

    // Receive args from other Fragments to get the name and floor parking place
    // Detail can see on res/navigation/mobile_navigation
    private val args: ParkingFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentParkingBinding.inflate(inflater, container, false)
        parkingViewModel.requestToken() // request token before call private API
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parkingAdapter = ParkingAdapter()

        val namePlace = args.floorAndClusters.namePlace
        val floorPlace = args.floorAndClusters.floor

        // Load token from local data store
        // This token for call private API in the server
        parkingViewModel.loadTokenFromDataStore().observe(viewLifecycleOwner) { token ->

            // Load data from the server to get all slot on specific floor parking place
            // Response will be checking, if not null, data will be execute it based on the state
            parkingViewModel.loadOccupancyFloor(token, namePlace, floorPlace)
                .observe(viewLifecycleOwner) { response ->
                    if (response != null) {
                        when (response) {
                            is Resource.Loading -> onLoading()
                            is Resource.Success -> showResult(response.data)
                            is Resource.Error -> showInfo()
                        }
                    }
                }
        }

        // Set layout manager and adapter to the recycler view
        // Using GridLayout with each row have four data
        binding.rvSpace.apply {
            layoutManager = GridLayoutManager(requireContext(), 4)
            adapter = parkingAdapter
        }

        binding.tvTitleName.text = args.floorAndClusters.namePlace
        binding.tvNameFloor.text = getString(
            R.string.carkir_parking_layout_name_parking,
            args.floorAndClusters.rangeClusters
        )
    }

    /**
     * Handle state when response is Success
     * Hide the others state and put the data to the adapter
     * */
    private fun showResult(data: List<OccupancyPlaceResponseItem>?) {
        onLoading(false)
        if (data?.isNotEmpty() as Boolean) {
            showInfo(false)
            binding.tvTotalSpace.text = getString(
                R.string.carkir_parking_layout_total_empty_slot,
                data[0].floorAvailability
            )
            parkingAdapter.submitList(data)
        } else {
            binding.tvTotalSpace.text =
                getString(R.string.carkir_parking_layout_total_empty_slot, 0)
            showInfo(true)
        }
    }

    /**
     * Handle state when response is Loading.
     * Show the progress bar
     * */
    private fun onLoading(isLoading: Boolean = true) {
        binding.progressBar.isVisible = isLoading
    }

    /**
     * Handle state when response is Error.
     * Show info with the message
     * */
    private fun showInfo(isShow: Boolean = true) {
        binding.ivEmptyParking.isVisible = isShow
        binding.tvEmptyParkingDescription.isVisible = isShow
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