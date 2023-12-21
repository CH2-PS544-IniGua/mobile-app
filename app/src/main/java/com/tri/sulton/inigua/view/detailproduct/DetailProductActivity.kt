package com.tri.sulton.inigua.view.detailproduct

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.tri.sulton.inigua.data.api.model.response.CatalogItem
import com.tri.sulton.inigua.databinding.ActivityDetailProductBinding
import com.tri.sulton.inigua.helper.Constant.currency
import com.tri.sulton.inigua.view.ViewModelFactory

class DetailProductActivity : AppCompatActivity() {

    private var _binding: ActivityDetailProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DetailProductViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setupObservation()
    }

    private fun setupObservation() {
        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.getProduct(intent.getStringExtra(ID) ?: "").observe(this) { product ->
            if (product != null) {
                setupView(product)
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
            title.text = catalogItem.title
            Glide.with(this@DetailProductActivity)
                .load(catalogItem.image)
                .into(image)
            price.text = currency(catalogItem.price)
            shortDesc.text = catalogItem.short_desc
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