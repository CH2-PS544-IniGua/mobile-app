package com.tri.sulton.inigua.view.upload

import androidx.lifecycle.ViewModel
import com.tri.sulton.inigua.data.UserRepository
import java.io.File

class UploadViewModel(private val repository: UserRepository) : ViewModel() {
    fun uploadImage(file: File, description: String) = repository.uploadImage(file, description)
}