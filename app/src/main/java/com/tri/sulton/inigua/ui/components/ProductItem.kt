package com.tri.sulton.inigua.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.tri.sulton.inigua.R
import com.tri.sulton.inigua.ui.theme.Grey100
import com.tri.sulton.inigua.ui.theme.IniGuaTheme

@Composable
fun ProductItem(
    imageUrl: String,
    name: String,
    price: Int,
    category: String,
    size: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(200.dp)
        )
        CategorySize(
            category,
            size,
            modifier = Modifier
                .padding(top = 8.dp, start = 12.dp, end = 12.dp)
                .fillMaxWidth()
        )
        Text(
            text = name.uppercase(),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 16.sp,
                letterSpacing = 0.1.sp,
                fontSize = 13.sp
            ),
            modifier = Modifier
                .padding(top = 8.dp, start = 12.dp, end = 12.dp)

        )
        Text(
            text = stringResource(id = R.string.rupiah, price),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp
            ),
            modifier = Modifier
                .padding(top = 8.dp, start = 12.dp, end = 12.dp, bottom = 24.dp)

        )
    }
}

@Composable
fun CategorySize(
    category: String,
    size: String,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = category,
            style = MaterialTheme.typography.titleSmall,
            color = Grey100,
            fontSize = 12.sp
        )
        Text(
            text = size,
            style = MaterialTheme.typography.titleSmall,
            color = Grey100,
            fontSize = 12.sp
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ProductItemPreview() {
    IniGuaTheme {
        ProductItem(
            "https://image.uniqlo.com/UQ/ST3/id/imagesgoods/463643/item/idgoods_37_463643.jpg?width=750",
            "Mercerized Cotton A Line Short Sleeve Dress",
            399000,
            "Women",
            "XS-XL"
        )
    }
}

