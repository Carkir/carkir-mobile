package com.bangkit.capstone.carkirapp.ui.parking

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.GridLayoutManager
import com.bangkit.capstone.carkirapp.R
import com.bangkit.capstone.carkirapp.data.Resource
import com.bangkit.capstone.carkirapp.data.remote.response.OccupancyPlaceResponseItem
import com.bangkit.capstone.carkirapp.databinding.ActivityParkingLayoutBinding
import com.bangkit.capstone.carkirapp.model.FloorAndClusterModel
import com.bangkit.capstone.carkirapp.model.ViewModelFactory
import com.bangkit.capstone.carkirapp.ui.adapter.ParkingAdapter
import com.bangkit.capstone.carkirapp.utils.calculateAutoColumns

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ParkingLayoutActivity : AppCompatActivity() {

    // Declaration for adapter
    private lateinit var parkingAdapter: ParkingAdapter

    // Declaration for binding view
    private lateinit var binding: ActivityParkingLayoutBinding

    // View Model initialization using delegate by viewModels
    private val parkingViewModel: ParkingViewModel by viewModels {
        ViewModelFactory.getInstance(this.applicationContext, dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParkingLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set title action bar and display icon back
        supportActionBar?.title = getString(R.string.carkir_title_parking_layout)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // request token before call private API
        parkingViewModel.requestToken()

        parkingAdapter = ParkingAdapter()
        val intent =
            intent?.getParcelableExtra<FloorAndClusterModel>(EXTRA_ITEM) as FloorAndClusterModel

        val namePlace = intent.namePlace
        val floorPlace = intent.floor

        // Load token from local data store
        // This token for call private API in the server
        parkingViewModel.loadTokenFromDataStore().observe(this) { token ->

            // Load data from the server to get all slot on specific floor parking place
            // Response will be checking, if not null, data will be execute it based on the state
            parkingViewModel.loadOccupancyFloor(token, namePlace, floorPlace)
                .observe(this) { response ->
                    if (response != null) {
                        when (response) {
                            is Resource.Loading -> onLoading()
                            is Resource.Success -> showResult(response.data)
                            is Resource.Error -> onError()
                        }
                    }
                }
        }

        // Set layout manager and adapter to the recycler view
        // Using GridLayout with each row have four data
        binding.rvSpace.apply {
            val autoColumns = calculateAutoColumns(this, 74)
            layoutManager = GridLayoutManager(this@ParkingLayoutActivity, autoColumns)
            adapter = parkingAdapter
        }

        binding.tvTitleName.text = intent.namePlace
        binding.tvNameFloor.text =
            getString(R.string.carkir_parking_layout_name_parking, intent.rangeClusters)
    }

    /**
     * Handle state when response is Success
     * Hide the others state and put the data to the adapter
     * */
    private fun showResult(data: List<OccupancyPlaceResponseItem>?) {
        if (data.isNullOrEmpty()) {
            showInfo(isProgressBarShow = false, isImageShow = true, isTextShow = true)
            binding.tvTotalSpace.text =
                getString(R.string.carkir_parking_layout_total_empty_slot, 0)
            return
        }

        // Hide info for state Error and Loading
        showInfo(isProgressBarShow = false, isImageShow = false, isTextShow = false)
        val availableSlot = data[0].floorAvailability
        binding.tvTotalSpace.text =
            getString(R.string.carkir_parking_layout_total_empty_slot, availableSlot)
        parkingAdapter.submitList(data)
    }

    /**
     * Handle state when response is Loading.
     * Show the progress bar
     * */
    private fun onLoading() {
        showInfo(isProgressBarShow = true, isImageShow = false, isTextShow = false)
    }

    /**
     * Handle state when response is Error.
     * Show image and text info
     * */
    private fun onError() {
        showInfo(isProgressBarShow = false, isImageShow = true, isTextShow = true)
    }

    /**
     * Handle state when response is Error.
     * Show info with the message
     * */
    private fun showInfo(isProgressBarShow: Boolean, isImageShow: Boolean, isTextShow: Boolean) {
        binding.progressBar.isVisible = isProgressBarShow
        binding.ivEmptyParking.isVisible = isImageShow
        binding.tvEmptyParkingDescription.isVisible = isTextShow
    }

    /**
     * Handle icon back navigation on actionbar
     * */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_ITEM = "extra_item"
    }
}