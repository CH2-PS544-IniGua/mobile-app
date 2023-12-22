package com.tri.sulton.inigua.view.history

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tri.sulton.inigua.data.api.model.response.HistoryItem
import com.tri.sulton.inigua.databinding.FragmentHistoryBinding
import com.tri.sulton.inigua.view.ViewModelFactory
import com.tri.sulton.inigua.view.detailproduct.DetailProductActivity

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupAdapter()
    }

    private fun setupRecyclerView() {
        with(binding) {
            historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            historyRecyclerView.setHasFixedSize(true)
        }
    }

    private fun setupAdapter() {
        val adapter = HistoryAdapter()
        binding.historyRecyclerView.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter { adapter.retry() }
        )

        viewModel.history().observe(viewLifecycleOwner) { pagingData ->
            adapter.submitData(lifecycle, pagingData)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
