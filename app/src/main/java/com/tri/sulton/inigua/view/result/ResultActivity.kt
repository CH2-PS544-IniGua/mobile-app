package com.tri.sulton.inigua.view.result

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tri.sulton.inigua.data.api.model.response.CatalogItem
import com.tri.sulton.inigua.data.api.model.response.UploadResponse
import com.tri.sulton.inigua.databinding.ActivityResultBinding
import com.tri.sulton.inigua.helper.Constant
import com.tri.sulton.inigua.view.ViewModelFactory
import com.tri.sulton.inigua.view.components.CatalogItem
import com.tri.sulton.inigua.view.detailproduct.DetailProductActivity
import com.tri.sulton.inigua.view.detailproduct.DetailProductViewModel
import com.tri.sulton.inigua.view.screen.ResultScreen

class ResultActivity : AppCompatActivity() {
    private var _binding: ActivityResultBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DetailProductViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val result = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<UploadResponse>(EXTRA_RESULT, UploadResponse::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<UploadResponse>(EXTRA_RESULT)
        }

        if (result != null) {
            viewModel.getClothingRecommendation(result.color_skin)
            binding.composeView.setContent {
                MaterialTheme {
                    ResultScreen(
                        predict_image = result.predict_image,
                        color_skin = result.color_skin,
                        color_top = result.color_top,
                        color_bottom = result.color_bottom,
                        percentage_clothes_pants = result.percentage_clothes_pants,
                        percentage_skin_clothes = result.percentage_skin_clothes
                    )
                }
            }
        }

        viewModel.clothingRecommendation.observe(this) {
            if (it != null && it.isNotEmpty()) {
                setupClothingRecommendation(it)
            }
        }
    }

    private fun setupClothingRecommendation(catalog: List<CatalogItem>) {
        binding.recommendation.setContent {
            MaterialTheme {
                Column() {
                    Text(text = "Clothing Recommendation")
                    LazyRow() {
                        items(catalog, key = { it.id }) { data ->
                            CatalogItem(
                                imageUrl = data.image,
                                name = data.title,
                                price = Constant.currency(data.price),
                                category = data.type,
                                size = "XL",
                                modifier = Modifier
                                    .width(200.dp)
                                    .clickable {
                                        val intent = Intent(this@ResultActivity, DetailProductActivity::class.java)
                                        intent.putExtra(DetailProductActivity.ID, data.id)
                                        startActivity(intent)
                                    }
                            )
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_RESULT = "extra_result"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}