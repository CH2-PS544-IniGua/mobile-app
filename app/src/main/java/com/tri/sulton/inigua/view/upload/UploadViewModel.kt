package com.tri.sulton.inigua.view.upload

import androidx.lifecycle.ViewModel
import com.tri.sulton.inigua.data.UploadRepository
import com.tri.sulton.inigua.data.UserRepository
import java.io.File

class UploadViewModel(private val repository: UploadRepository) : ViewModel() {
    fun uploadImage(file: File, username: String) = repository.uploadImage(file, username)
}