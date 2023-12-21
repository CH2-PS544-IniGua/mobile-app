package com.tri.sulton.inigua.di

import android.content.Context
import com.tri.sulton.inigua.data.UserRepository
import com.tri.sulton.inigua.data.api.retrofit.ApiConfig
import com.tri.sulton.inigua.data.database.IniGuaDatabase
import com.tri.sulton.inigua.data.pref.UserPreference
import com.tri.sulton.inigua.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        val database = IniGuaDatabase.getDatabase(context)
        return UserRepository.getInstance(pref, database, apiService)
    }
}