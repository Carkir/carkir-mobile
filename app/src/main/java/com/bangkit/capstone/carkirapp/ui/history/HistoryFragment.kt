package com.bangkit.capstone.carkirapp.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bangkit.capstone.carkirapp.databinding.FragmentHistoryBinding
import com.bangkit.capstone.carkirapp.model.ViewModelFactory
import com.bangkit.capstone.carkirapp.ui.adapter.HistoryAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder

// TODO TO HIDE ACTION BAR
class HistoryFragment : Fragment() {

    // Declaration for binding view
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    // View Model initialization using delegate by viewModels
    private val historyViewModel: HistoryViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)

        // Trigger click for delete all histories
        binding.fabDeleteAll.setOnClickListener {
            showAlertDialog(
                "Delete all your histories?",
                historyViewModel.deleteAllHistory()
            )
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Declaration adapter for recycler view histories parking place
        // The lambda function is use for show alert dialog before delete the history
        val historyAdapter = HistoryAdapter { place ->
            showAlertDialog(
                "Are you sure want to delete?",
                historyViewModel.deleteHistoryPlace(place)
            )
        }

        // Load data from the local database to get list history parking place
        // Data will be checking, if empty show the info and if not show list of parking place
        // and hide the FloatingActionButton to avoid bug for delete nothing.
        historyViewModel.loadAllHistory().observe(viewLifecycleOwner) { data ->
            if (data.isNotEmpty()) {
                historyAdapter.submitList(data)
                binding.emptyHistories.isVisible = false
            } else {
                binding.emptyHistories.isVisible = true
                binding.fabDeleteAll.isVisible = false
            }
        }

        // Set adapter to recycler view
        binding.rvHistory.adapter = historyAdapter
    }

    /**
     * Show alert dialog for delete one or all history
     * */
    private fun showAlertDialog(title: String, action: Unit) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage("Your history will be deleted and cannot to restore it.")
            .setPositiveButton("Delete") { _, _ -> action }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            .show()
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