package com.tri.sulton.inigua.view.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.tri.sulton.inigua.view.components.ColorRect
import com.tri.sulton.inigua.view.components.PercentageCircle

@Composable
fun ResultScreen(
    predict_image: String,
    color_skin: String,
    color_top: String,
    color_bottom: String,
    percentage_clothes_pants: Int,
    percentage_skin_clothes: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        AsyncImage(
            model = predict_image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
        )
        Text(
            text = "COLOR"
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.LightGray)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ColorRect(title = "Top", color = color_top)
            ColorRect(title = "Skin", color = color_skin)
            ColorRect(title = "Bottom", color = color_bottom)
        }
        Text(
            text = "PERCENTAGE"
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.LightGray)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PercentageCircle(
                title = "SKIN & CLOTHES",
                number = percentage_skin_clothes,
            )
            PercentageCircle(
                title = "CLOTHES & PANTS",
                number = percentage_clothes_pants,
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PercentageCircle(
                title = "OVERALL",
                number = when (percentage_clothes_pants) {
                     0 -> percentage_skin_clothes
                    else -> (percentage_skin_clothes + percentage_clothes_pants) / 2
                                                         },
            )
        }

    }
}

@Composable
@Preview(showBackground = true)
fun ResultScreenPreview() {
    MaterialTheme {
        ResultScreen(
            "https://storage.googleapis.com/application-inigua/fashion/3sulton/IMG_20200926_165844-20231222055806.jpg",
            "Brown",
            "Blue",
            "None",
            0,
            87
        )
    }
}

