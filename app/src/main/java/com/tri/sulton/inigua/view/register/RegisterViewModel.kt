package com.tri.sulton.inigua.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tri.sulton.inigua.data.UserRepository
import com.tri.sulton.inigua.data.api.model.Register
import com.tri.sulton.inigua.data.api.model.response.CommonResponse
import com.tri.sulton.inigua.helper.Constant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _registerResponse = MutableLiveData<CommonResponse<String>>()
    val registerResponse: LiveData<CommonResponse<String>> = _registerResponse

    fun register(data: Register) {
        _isLoading.value = true
        val client = repository.register(data)
        client.enqueue(object: Callback<CommonResponse<String>> {
            override fun onResponse(
                call: Call<CommonResponse<String>>,
                response: Response<CommonResponse<String>>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _registerResponse.value = responseBody
                } else {
                    _registerResponse.value = CommonResponse<String>("error",
                        Constant.getErrorResponse(response.errorBody()!!.string()).message,
                        ""
                    )
                }
            }

            override fun onFailure(call: Call<CommonResponse<String>>, t: Throwable) {
                _isLoading.value = false
                _registerResponse.value = CommonResponse("error", t.message.toString(), "")
            }
        })
    }

}