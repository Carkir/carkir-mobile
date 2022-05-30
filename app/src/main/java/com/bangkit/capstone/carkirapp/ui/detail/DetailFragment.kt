package com.bangkit.capstone.carkirapp.ui.detail

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.bangkit.capstone.carkirapp.R
import com.bangkit.capstone.carkirapp.data.Resource
import com.bangkit.capstone.carkirapp.data.local.entity.HistoryEntity
import com.bangkit.capstone.carkirapp.data.remote.response.DetailPlaceResponse
import com.bangkit.capstone.carkirapp.databinding.FragmentDetailBinding
import com.bangkit.capstone.carkirapp.model.FloorAndClusterModel
import com.bangkit.capstone.carkirapp.model.ViewModelFactory
import com.bangkit.capstone.carkirapp.ui.adapter.FloorAndClusterAdapter
import com.bangkit.capstone.carkirapp.utils.decodeBase64ToBitmap
import com.bangkit.capstone.carkirapp.utils.loadImage

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DetailFragment : Fragment() {

    // Declaration for adapter
    private lateinit var floorAdapter: FloorAndClusterAdapter

    // Declaration for binding view
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    // View Model initialization using delegate by viewModels
    private val detailViewModel: DetailViewModel by viewModels {
        ViewModelFactory.getInstance(
            requireContext(),
            requireContext().dataStore
        )
    }

    // Receive args from other Fragments to get the name of parking place
    // Detail can see on res/navigation/mobile_navigation
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        detailViewModel.requestToken() // request token before call private API
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val namePlace = args.namePlace
        floorAdapter = FloorAndClusterAdapter()

        // Load token from local data store
        // This token for call private API in the server
        detailViewModel.loadTokenFromDataStore().observe(viewLifecycleOwner) { token ->

            // Load data from the server to get detail data parking place
            // Response will be checking, if not null, data will be execute it based on the state
            detailViewModel.loadDetailParkingPlace(token, namePlace)
                .observe(viewLifecycleOwner) { response ->
                    if (response != null) {
                        when (response) {
                            is Resource.Loading -> {} // TODO HANDLE LOADING STATE
                            is Resource.Success -> showDetailPlace(response.data)
                            is Resource.Error -> {
                                Log.d("DetailFragment", response.message.toString())
                            } // TODO HANDLE ERROR STATE
                        }
                    }
                }
        }

        binding.fabFavoriteLocation.setOnClickListener {
            // TODO ADD OR REMOVE FAVORITE PARKING PLACE
        }

        // Set adapter to recycler view
        binding.rvClustersParking.adapter = floorAdapter
    }

    /**
     * Handle state when response is Success.
     * The data response will be binding to the layout.
     * */
    private fun showDetailPlace(data: DetailPlaceResponse?) {
        if (data == null) return

        val (priceLow, name, location, time, totalEmptySpace, priceHigh, status, alamat, image) = data
        binding.apply {
            // TODO UNCOMMENT THE CODE IF IMAGE IS FIXED
            // val bitmap = image.decodeBase64ToBitmap()
            // ivPlaceDetail.loadImage(requireContext(), bitmap)
            tvStatusPlaceDetail.text = status
            tvTimePlaceDetail.text = time
            tvNamePlaceDetail.text = name
            tvAddressPlaceDetail.text = alamat
            tvPriceFirstHour.text = getString(R.string.carkir_detail_price_low, priceLow)
            tvPriceNextHour.text = getString(R.string.carkir_detail_price_high, priceHigh)
            tvTotalEmptySpacePlaceDetail.text =
                getString(R.string.carkir_detail_title_space, totalEmptySpace)
        }

        // Add history parking place
        detailViewModel.addPlaceToHistory(
            HistoryEntity(
                name = name,
                totalSpace = totalEmptySpace,
                time = time
            )
        )

        showFloorAndClusters(name, location)
    }

    /**
     * Set data to the adapter recycler view
     * if data is empty just show info message
     * */
    private fun showFloorAndClusters(namePlace: String, floorClusters: List<String>) {
        if (floorClusters.isNullOrEmpty()) {
            binding.tvEmptyClustersParking.isVisible = true
        } else {
            binding.tvEmptyClustersParking.isVisible = false
            floorAdapter.submitList(modifiedFloorClusters(namePlace, floorClusters))
        }
    }

    // TODO DOCUMENTATION
    private fun modifiedFloorClusters(
        name: String,
        cluster: List<String>
    ): List<FloorAndClusterModel> {
        val floorAndClusters = ArrayList<FloorAndClusterModel>()
        var floor = '1'
        val rangeClusters = StringBuilder()
        var totalParkingSpace = 0

        // Add all floor and cluster, except the last floor and clusters
        for (i in 1 until cluster.size) {
            if (floor == cluster[i][0]) {
                rangeClusters.append(cluster[i][1])
                totalParkingSpace += (cluster[i][2]).toString().toInt()
            } else {
                val data = FloorAndClusterModel(
                    namePlace = name,
                    floor = floor.toString().toInt(),
                    rangeClusters = "$floor${rangeClusters.first()} - $floor${rangeClusters.last()}",
                    parkingSpace = totalParkingSpace
                )
                floorAndClusters.add(data)

                rangeClusters.clear()
                totalParkingSpace = 0
                floor = cluster[i][0]
                rangeClusters.append(cluster[i][1])
                totalParkingSpace += (cluster[i][2]).toString().toInt()
            }
        }

        // Add last floor and range clusters
        val lastData = FloorAndClusterModel(
            namePlace = name,
            floor = floor.toString().toInt(),
            rangeClusters = "$floor${rangeClusters.first()} - $floor${rangeClusters.last()}",
            parkingSpace = totalParkingSpace
        )
        floorAndClusters.add(lastData)

        return floorAndClusters
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