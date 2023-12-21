package com.tri.sulton.inigua.view.catalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tri.sulton.inigua.data.UserRepository
import com.tri.sulton.inigua.data.api.model.response.CatalogItem
import com.tri.sulton.inigua.data.pref.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun changeApiService(token: String) {
        repository.changeApiService(token)
    }

    private val productsLiveData: LiveData<PagingData<CatalogItem>> by lazy {
        return@lazy repository.getProducts().cachedIn(viewModelScope)
    }

    fun products() = productsLiveData

}