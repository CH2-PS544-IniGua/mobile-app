package com.tri.sulton.inigua.view.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tri.sulton.inigua.data.UserRepository
import com.tri.sulton.inigua.data.api.model.response.HistoryItem

class HistoryViewModel(private val repository: UserRepository) : ViewModel() {

    private val historyLiveData: LiveData<PagingData<HistoryItem>> by lazy {
        repository.getHistory().cachedIn(viewModelScope)
    }

    fun history() = historyLiveData
}
