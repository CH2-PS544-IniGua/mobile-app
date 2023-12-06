package com.tri.sulton.inigua.ui.screen.cart

import com.tri.sulton.inigua.model.OrderProduct

data class CartState(
    val orderProduct: List<OrderProduct>,
    val totalPrice: Int
)