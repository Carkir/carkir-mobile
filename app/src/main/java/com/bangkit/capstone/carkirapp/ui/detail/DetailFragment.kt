package com.bangkit.capstone.carkirapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bangkit.capstone.carkirapp.R
import com.bangkit.capstone.carkirapp.data.Resource
import com.bangkit.capstone.carkirapp.data.remote.response.DetailPlaceResponse
import com.bangkit.capstone.carkirapp.databinding.FragmentDetailBinding
import com.bangkit.capstone.carkirapp.model.ViewModelFactory

class DetailFragment : Fragment() {

    // Declaration for binding view
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    // View Model initialization using delegate by viewModels
    private val detailViewModel: DetailViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    // Receive args from other Fragments to get the name of parking place
    // Detail can see on res/navigation/mobile_navigation
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val namePlace = args.namePlace

        // Load data from the server to get detail data parking place
        // Response will be checking, if not null, data will be execute it based on the state
        detailViewModel.loadDetailParkingPlace(namePlace).observe(viewLifecycleOwner) { response ->
            if (response != null) {
                when (response) {
                    is Resource.Loading -> {} // TODO HANDLE LOADING STATE
                    is Resource.Success -> showDetailPlace(response.data)
                    is Resource.Error -> {} // TODO HANDLE ERROR STATE
                }
            }
        }

        binding.fabFavoriteLocation.setOnClickListener {
            // TODO ADD OR REMOVE FAVORITE PARKING PLACE
        }
    }

    /**
     * Handle state when response is Success.
     * The data response will be binding to the layout.
     * */
    private fun showDetailPlace(data: DetailPlaceResponse?) {
        val (priceLow, name, time, totalEmptySpace, priceHigh, status) = data as DetailPlaceResponse
        binding.apply {
            tvStatusPlaceDetail.text = status
            tvTimePlaceDetail.text = time
            tvNamePlaceDetail.text = name
            tvPriceFirstHour.text = getString(R.string.carkir_detail_price_low, priceLow)
            tvPriceNextHour.text = getString(R.string.carkir_detail_price_low, priceHigh)
            tvTotalEmptySpacePlaceDetail.text =
                getString(R.string.carkir_detail_title_space, totalEmptySpace)
        }
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