package com.tri.sulton.inigua.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tri.sulton.inigua.data.UserRepository
import com.tri.sulton.inigua.data.api.model.Login
import com.tri.sulton.inigua.data.api.model.response.LoginResponse
import com.tri.sulton.inigua.data.pref.UserModel
import com.tri.sulton.inigua.helper.Constant
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun login(data: Login) {
        _isLoading.value = true
        val client = repository.login(data)
        client.enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _loginResponse.value = responseBody
                } else {
                    _loginResponse.value = LoginResponse(
                        status = "error",
                        message = Constant.getErrorResponse(response.errorBody()!!.string()).message,
                        data = "",
                        username = ""
                    )
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _loginResponse.value = LoginResponse(
                    status = "error",
                    message = t.message.toString(),
                    data = "",
                    username = ""
                )
            }
        })
    }

}