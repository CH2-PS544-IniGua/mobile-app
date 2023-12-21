package com.tri.sulton.inigua.view.detailstory

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.tri.sulton.inigua.R
import com.tri.sulton.inigua.data.api.model.response.CatalogItem
import com.tri.sulton.inigua.databinding.ActivityDetailStoryBinding
import com.tri.sulton.inigua.helper.timeSince
import com.tri.sulton.inigua.view.ViewModelFactory

class DetailStoryActivity : AppCompatActivity() {

    private var _binding: ActivityDetailStoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DetailStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservation()
    }

    private fun setupObservation() {
        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.getStory(intent.getStringExtra(ID) ?: "").observe(this) { story ->
            if (story != null) {
                setupView(story)
            }
        }

        viewModel.errorResponse.observe(this) { errorResponse ->
            if (errorResponse.error) {
                Toast.makeText(this, errorResponse.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupView(catalogItem: CatalogItem) {
        with(binding) {
            tvName.text = catalogItem.title
            Glide.with(this@DetailStoryActivity)
                .load(catalogItem.image)
                .into(imgStory)
            tvDescription.text = catalogItem.short_desc
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val ID = "id"
    }
}