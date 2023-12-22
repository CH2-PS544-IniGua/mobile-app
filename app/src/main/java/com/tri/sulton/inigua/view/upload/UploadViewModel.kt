package com.tri.sulton.inigua.view.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tri.sulton.inigua.data.UploadRepository
import com.tri.sulton.inigua.data.UserRepository
import com.tri.sulton.inigua.data.pref.UserModel
import java.io.File

class UploadViewModel(private val repository: UploadRepository) : ViewModel() {
    fun uploadImage(file: File, username: String) = repository.uploadImage(file, username)

    var username = ""

    init {
        getSession().observeForever {
            username = it.username
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}