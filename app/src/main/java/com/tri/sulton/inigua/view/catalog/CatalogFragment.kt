package com.tri.sulton.inigua.view.catalog

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tri.sulton.inigua.R
import com.tri.sulton.inigua.data.api.model.response.CatalogItem
import com.tri.sulton.inigua.databinding.FragmentCatalogBinding
import com.tri.sulton.inigua.view.ViewModelFactory
import com.tri.sulton.inigua.view.detailstory.DetailStoryActivity
import com.tri.sulton.inigua.view.upload.UploadActivity
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
//                setupView()
                setupAction()
                setupRecyclerView()
                setupAdapter()
            }
        }


    }

    private fun setupRecyclerView() {
        with(binding) {
            rv.layoutManager = LinearLayoutManager(requireContext())
            rv.setHasFixedSize(true)
        }
    }

    private fun setupAdapter() {
        val adapter = StoryAdapter()
        binding.rv.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter { adapter.retry() }
        )

        viewModel.stories().observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
            adapter.setOnItemClickCallback(
                object : StoryAdapter.OnItemClickCallback {
                    override fun onItemClicked(item: CatalogItem) {
                        val intent = Intent(requireContext(), DetailStoryActivity::class.java)
                        intent.putExtra(DetailStoryActivity.ID, item.id)
                        startActivity(intent)
                    }
                }
            )
        }
    }

    private fun setupAction() {
        binding.fab.setOnClickListener {
            startActivity(Intent(requireContext(), UploadActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}