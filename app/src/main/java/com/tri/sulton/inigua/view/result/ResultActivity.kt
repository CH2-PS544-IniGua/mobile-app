package com.tri.sulton.inigua.view.result

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.tri.sulton.inigua.R
import com.tri.sulton.inigua.data.api.model.response.UploadResponse
import com.tri.sulton.inigua.view.screen.ResultScreen

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_result)

        supportActionBar?.hide()

        val result = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<UploadResponse>(EXTRA_RESULT, UploadResponse::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<UploadResponse>(EXTRA_RESULT)
        }

        if (result != null) {
            Toast.makeText(this, result.predict_image, Toast.LENGTH_SHORT).show()
        }

        setContent {
            MaterialTheme {
                ResultScreen(
                    predict_image = result?.predict_image ?: "",
                    color_skin = result?.color_skin ?: "",
                    color_top = result?.color_top ?: "",
                    color_bottom = result?.color_bottom ?: "",
                    percentage_clothes_pants = result?.percentage_clothes_pants ?: 0,
                    percentage_skin_clothes = result?.percentage_skin_clothes ?: 0
                )
            }
        }
    }

    companion object {
        const val EXTRA_RESULT = "extra_result"
    }
}