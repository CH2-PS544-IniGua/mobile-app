package com.tri.sulton.inigua.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tri.sulton.inigua.helper.Constant

@Composable
fun ColorRect(
    title: String,
    color: String,
    modifier: Modifier = Modifier
) {
    Column(
    ) {
        Text(
            text = "$title: $color",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
        )
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color(Constant.hexOf(color)))
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ColorRectPreview() {
    MaterialTheme {
        ColorRect(
            title = "Color",
            color = "Red",
        )
    }
}