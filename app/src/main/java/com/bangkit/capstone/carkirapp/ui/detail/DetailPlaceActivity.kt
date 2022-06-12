package com.bangkit.capstone.carkirapp.ui.detail

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.navArgs
import com.bangkit.capstone.carkirapp.R
import com.bangkit.capstone.carkirapp.data.Resource
import com.bangkit.capstone.carkirapp.data.local.entity.PlacesEntity
import com.bangkit.capstone.carkirapp.data.remote.response.DetailPlaceResponse
import com.bangkit.capstone.carkirapp.databinding.ActivityDetailPlaceBinding
import com.bangkit.capstone.carkirapp.model.FloorAndClusterModel
import com.bangkit.capstone.carkirapp.model.ViewModelFactory
import com.bangkit.capstone.carkirapp.ui.adapter.FloorAndClusterAdapter
import com.bangkit.capstone.carkirapp.utils.decodeBase64ToBitmap
import com.bangkit.capstone.carkirapp.utils.loadImage

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DetailPlaceActivity : AppCompatActivity() {

    // Declaration for adapter
    private lateinit var floorAdapter: FloorAndClusterAdapter

    // Declaration for binding view
    private lateinit var binding: ActivityDetailPlaceBinding

    // View Model initialization using delegate by viewModels
    private val detailViewModel: DetailViewModel by viewModels {
        ViewModelFactory.getInstance(this.applicationContext, dataStore)
    }

    // Receive args from other Fragments to get the name of parking place
    // Detail can see on res/navigation/mobile_navigation
    private val args: DetailPlaceActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set title action bar and display icon back
        supportActionBar?.title = getString(R.string.carkir_title_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // request token before call private API
        detailViewModel.requestToken()

        val namePlace = args.namePlace
        floorAdapter = FloorAndClusterAdapter()

        // Load token from local data store
        // This token for call private API in the server
        detailViewModel.loadTokenFromDataStore().observe(this) { token ->

            // Load data from the server to get detail data parking place
            // Response will be checking, if not null, data will be execute it based on the state
            detailViewModel.loadDetailParkingPlace(token, namePlace)
                .observe(this) { response ->
                    if (response != null) {
                        when (response) {
                            is Resource.Loading -> onLoading()
                            is Resource.Success -> showDetailPlace(response.data)
                            is Resource.Error -> onError()
                        }
                    }
                }
        }

        // Set adapter to recycler view
        binding.rvClustersParking.adapter = floorAdapter
    }

    /**
     * Handle state when response is Error
     * Show image and text for information
     * */
    private fun onLoading() {
        showingInfo(isContainerShow = true, isMessageShow = false, isProgressBarShow = true)
    }

    /**
     * Handle state when response is Error
     * Show image and text for information
     * */
    private fun onError() {
        showingInfo(isContainerShow = true, isMessageShow = true, isProgressBarShow = false)
    }

    /**
     * Show or hide image and info text for all states
     * */
    private fun showingInfo(
        isContainerShow: Boolean,
        isMessageShow: Boolean,
        isProgressBarShow: Boolean
    ) {
        with(binding) {
            containerState.isVisible = isContainerShow
            ivErrorDetail.isVisible = isMessageShow
            tvErrorDetail.isVisible = isMessageShow
            tvErrorDescription.isVisible = isMessageShow
            progressBar.isVisible = isProgressBarShow
        }
    }

    /**
     * Handle state when response is Success.
     * The data response will be binding to the layout.
     * */
    private fun showDetailPlace(data: DetailPlaceResponse?) {
        if (data == null) {
            showingInfo(isContainerShow = true, isMessageShow = true, isProgressBarShow = false)
            return
        }

        showingInfo(isContainerShow = false, isMessageShow = false, isProgressBarShow = false)
        val (priceLow, name, clusterCount, time, totalEmptySpace, priceHigh, status, address, image, isFavorite) = data
        binding.apply {
            image?.let {
                val bitmap = it.decodeBase64ToBitmap()
                ivPlaceDetail.loadImage(this@DetailPlaceActivity, bitmap)
            }
            tvStatusPlaceDetail.text = status.replaceFirstChar { it.uppercase() }
            tvTimePlaceDetail.text = time
            tvNamePlaceDetail.text = name
            tvAddressPlaceDetail.text = address
            tvPriceFirstHour.text = getString(R.string.carkir_detail_price_low, priceLow)
            tvPriceNextHour.text = getString(R.string.carkir_detail_price_high, priceHigh)
            tvTotalEmptySpacePlaceDetail.text =
                getString(R.string.carkir_detail_title_space, totalEmptySpace)
        }

        // Listener and updating state favorite button
        var statusFavorite = isFavorite
        setStatusFavorite(statusFavorite)
        binding.fabFavoriteLocation.setOnClickListener {
            statusFavorite = !statusFavorite
            detailViewModel.updateFavoritePlace(name, statusFavorite)
            setStatusFavorite(statusFavorite)
        }

        // Add parking place to histories
        detailViewModel.addPlace(
            PlacesEntity(
                name,
                time,
                status,
                address,
                priceHigh,
                priceLow,
                totalEmptySpace,
                isAlreadySee = true,
                isFavorite = statusFavorite,
                image = image
            )
        )

        // Handle the data for adapter
        showFloorAndClusters(name, clusterCount)
    }

    /**
     * Changing icon favorite based on state status favorite
     * */
    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            binding.fabFavoriteLocation.setImageDrawable(
                ContextCompat.getDrawable(
                    this, R.drawable.ic_favorite_24
                )
            )
        } else { // If not favorite place
            binding.fabFavoriteLocation.setImageDrawable(
                ContextCompat.getDrawable(
                    this, R.drawable.ic_favorite_border_24
                )
            )
        }
    }

    /**
     * Set data to the adapter recycler view
     * if data is empty just show info message
     * */
    private fun showFloorAndClusters(namePlace: String, floorClusters: List<String>) {
        if (floorClusters.isNullOrEmpty() || floorClusters.size <= 1) {
            binding.tvEmptyClustersParking.isVisible = true
        } else {
            binding.tvEmptyClustersParking.isVisible = false
            floorAdapter.submitList(modifiedFloorClusters(namePlace, floorClusters))
        }
    }

    // TODO DOCUMENTATION AND REFACTORING
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
     * Handle icon back navigation on actionbar
     * */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}