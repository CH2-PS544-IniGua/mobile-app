package com.tri.sulton.inigua.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tri.sulton.inigua.di.Injection
import com.tri.sulton.inigua.model.OrderProduct
import com.tri.sulton.inigua.ui.ViewModelFactory
import com.tri.sulton.inigua.ui.common.UiState
import com.tri.sulton.inigua.ui.components.ProductItem
import com.tri.sulton.inigua.ui.components.SearchBar

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    ),
    navigateToDetail: (Long) -> Unit,
) {
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        val query by viewModel.query

        when (uiState) {
            is UiState.Loading -> {
                if (query.isEmpty()) {
                    viewModel.getAllProducts()
                } else {
                    viewModel.search(query)
                }
            }

            is UiState.Success -> {
                HomeContent(
                    orderProduct = uiState.data,
                    navigateToDetail = navigateToDetail,
                    viewModel = viewModel,
                    query = query,
                    modifier = modifier
                )
            }

            is UiState.Error -> {}
        }

    }
}

@Composable
fun HomeContent(
    orderProduct: List<OrderProduct>,
    navigateToDetail: (Long) -> Unit,
    viewModel: HomeViewModel,
    query: String = "",
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        SearchBar(
            query = query,
            onQueryChange = viewModel::search,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
        )
        if (orderProduct.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No products found", style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(160.dp),
                contentPadding = PaddingValues(0.dp),
                horizontalArrangement = Arrangement.spacedBy(0.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                items(orderProduct, key = { it.product.id }) { data ->
                    ProductItem(
                        imageUrl = data.product.imageUrl,
                        name = data.product.name,
                        price = data.product.price,
                        category = data.product.category,
                        size = data.product.size,
                        modifier = Modifier
                            .clickable {
                                navigateToDetail(data.product.id)
                            }
                    )
                }
            }
        }
    }
}