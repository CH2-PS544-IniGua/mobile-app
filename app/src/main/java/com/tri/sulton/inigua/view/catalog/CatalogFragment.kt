package com.tri.sulton.inigua.view.catalog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.tri.sulton.inigua.data.api.model.response.CatalogItem
import com.tri.sulton.inigua.databinding.FragmentCatalogBinding
import com.tri.sulton.inigua.view.ViewModelFactory
import com.tri.sulton.inigua.view.detailproduct.DetailProductActivity
import com.tri.sulton.inigua.view.welcome.WelcomeActivity

class CatalogFragment : Fragment() {

    private var _binding: FragmentCatalogBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCatalogBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user.token.isEmpty()) {
                startActivity(Intent(requireContext(), WelcomeActivity::class.java))
                activity?.finish()
            } else {
                viewModel.changeApiService(user.token)
                setupRecyclerView()
                setupAdapter()
            }
        }


    }

    private fun setupRecyclerView() {
        with(binding) {
            rv.layoutManager = GridLayoutManager(requireContext(), 2)
            rv.setHasFixedSize(true)
        }
    }

    private fun setupAdapter() {
        val adapter = ProductAdapter()
        binding.rv.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter { adapter.retry() }
        )

        viewModel.products().observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
            adapter.setOnItemClickCallback(
                object : ProductAdapter.OnItemClickCallback {
                    override fun onItemClicked(item: CatalogItem) {
                        val intent = Intent(requireContext(), DetailProductActivity::class.java)
                        intent.putExtra(DetailProductActivity.ID, item.id)
                        startActivity(intent)
                    }
                }
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}