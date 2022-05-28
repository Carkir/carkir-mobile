package com.bangkit.capstone.carkirapp.ui.parking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bangkit.capstone.carkirapp.data.Resource
import com.bangkit.capstone.carkirapp.data.remote.response.OccupancyPlaceResponseItem
import com.bangkit.capstone.carkirapp.databinding.FragmentParkingBinding
import com.bangkit.capstone.carkirapp.model.ViewModelFactory
import com.bangkit.capstone.carkirapp.ui.adapter.ParkingAdapter

class ParkingFragment : Fragment() {

    private lateinit var parkingAdapter: ParkingAdapter

    private var _binding: FragmentParkingBinding? = null
    private val binding get() = _binding!!

    private val parkingViewModel: ParkingViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentParkingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parkingAdapter = ParkingAdapter()

        val namePlace = "Hotel del Luna"
        val floorPlace = 1

        parkingViewModel.loadOccupancyFloor(namePlace, floorPlace)
            .observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    when (response) {
                        is Resource.Loading -> {}
                        is Resource.Success -> showResult(response.data)
                        is Resource.Error -> {}
                    }
                }
            }

        binding.rvSpace.apply {
            layoutManager = GridLayoutManager(requireContext(), 4)
            adapter = parkingAdapter
        }

    }

    private fun showResult(data: List<OccupancyPlaceResponseItem>?) {
//        if (data.isNotEmpty()) {
//            parkingAdapter.submitList(data)
//        }
        binding.tvTitleName.text = "Hotel del Luna"
        binding.tvNameFloor.text = "Lantai 1"
        binding.tvTotalSpace.text = "20 Tempat tersedia"

        parkingAdapter.submitList(data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}