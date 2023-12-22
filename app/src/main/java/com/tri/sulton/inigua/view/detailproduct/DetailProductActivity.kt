package com.tri.sulton.inigua.view.detailproduct

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bumptech.glide.Glide
import com.tri.sulton.inigua.data.api.model.response.CatalogItem
import com.tri.sulton.inigua.databinding.ActivityDetailProductBinding
import com.tri.sulton.inigua.helper.Constant.currency
import com.tri.sulton.inigua.view.ViewModelFactory
import com.tri.sulton.inigua.view.components.CatalogItem
import com.tri.sulton.inigua.view.components.ColorRect
import com.tri.sulton.inigua.view.components.Expandable


class DetailProductActivity : AppCompatActivity() {

    private var _binding: ActivityDetailProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DetailProductViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        _binding = ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservation()
    }

    private fun setupObservation() {
        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.errorResponse.observe(this) { errorResponse ->
            if (errorResponse.status == "error") {
                Toast.makeText(this, errorResponse.message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.getProduct(intent.getStringExtra(ID) ?: "").observe(this) { product ->
            if (product != null) {
                if (product.type == "clothes") {
                    viewModel.getPantsRecommendation(product.color)
                }
                setupView(product)
            }
        }

        viewModel.pantsRecommendation.observe(this) {
            if (it != null && it.isNotEmpty()) {
                setupPantsRecommendation(it)
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
            composeView.setContent {
                MaterialTheme {
                    Column() {
                        ColorRect(title = "Color", color = catalogItem.color)
                        Text(text = "DESCRIPTION")
                        Expandable(title = "Overview", description = catalogItem.overview)
                        Expandable(title = "Materials", description = catalogItem.material)
                    }
                }
            }
        }
    }

    private fun setupPantsRecommendation(catalog: List<CatalogItem>) {
        binding.recommendation.setContent {
            MaterialTheme {
                Column() {
                    Text(text = "Pants Recommendation")
                    LazyRow() {
                        items(catalog, key = { it.id }) { data ->
                            CatalogItem(
                                imageUrl = data.image,
                                name = data.title,
                                price = currency(data.price),
                                category = data.type,
                                size = "XL",
                                modifier = Modifier
                                    .width(200.dp)
                                    .clickable {
                                        val intent = Intent(this@DetailProductActivity, DetailProductActivity::class.java)
                                        intent.putExtra(ID, data.id)
                                        startActivity(intent)
                                    }
                            )
                        }
                    }
                }
            }
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