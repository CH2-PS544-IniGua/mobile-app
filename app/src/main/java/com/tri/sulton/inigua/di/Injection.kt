package com.tri.sulton.inigua.di

import com.tri.sulton.inigua.data.ProductRepository


object Injection {
    fun provideRepository(): ProductRepository {
        return ProductRepository.getInstance()
    }
}