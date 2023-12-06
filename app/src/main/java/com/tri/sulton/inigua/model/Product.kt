package com.tri.sulton.inigua.model

data class Product(
    val id: Long,
    val imageUrl: String,
    val name: String,
    val price: Int,
    val category: String,
    val size: String,
    val color: String,
    val description: String,
    val overview: String,
    val materials: String,
)