package com.tri.sulton.inigua.view.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tri.sulton.inigua.data.UserRepository
import com.tri.sulton.inigua.data.api.model.Register
import com.tri.sulton.inigua.data.api.model.response.CommonResponse
import com.tri.sulton.inigua.data.api.model.response.LoginResponse
import com.tri.sulton.inigua.helper.Constant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _registerResponse = MutableLiveData<LoginResponse>()
    val registerResponse: LiveData<LoginResponse> = _registerResponse

    fun register(data: Register) {
        _isLoading.value = true
        val client = repository.register(data)
        client.enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _registerResponse.value = responseBody
                } else {
                    _registerResponse.value = LoginResponse(
                        status = "error",
                        message = "Failed to create user: Username already taken",
                        data = "",
                        username = ""
                    )
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _registerResponse.value = LoginResponse(
                    status = "error",
                    message = t.message.toString(),
                    data = "",
                    username = ""
                )
            }
        })
    }

}