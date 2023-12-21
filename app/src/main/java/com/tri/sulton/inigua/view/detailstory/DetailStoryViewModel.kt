package com.tri.sulton.inigua.view.detailstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tri.sulton.inigua.data.UserRepository
import com.tri.sulton.inigua.data.api.model.response.CatalogItem
import com.tri.sulton.inigua.data.api.model.response.CommonResponse
import com.tri.sulton.inigua.data.api.model.response.ErrorResponse
import com.tri.sulton.inigua.helper.Constant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailStoryViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorResponse = MutableLiveData<ErrorResponse>()
    val errorResponse: LiveData<ErrorResponse> = _errorResponse

    var id = ""

    private val storyLiveData: LiveData<CatalogItem> by lazy {
        val story = MutableLiveData<CatalogItem>()
        _isLoading.value = true
        val client = repository.getDetailStory(id)
        client.enqueue(object : Callback<CommonResponse<CatalogItem>> {
            override fun onResponse(
                call: Call<CommonResponse<CatalogItem>>,
                response: Response<CommonResponse<CatalogItem>>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    story.value = responseBody.data

                } else {
                    _errorResponse.value = Constant.getErrorResponse(response.errorBody()!!.string())
                }
            }

            override fun onFailure(call: Call<CommonResponse<CatalogItem>>, t: Throwable) {
                _isLoading.value = false
                _errorResponse.value = ErrorResponse(
                    error = true,
                    message = t.message.toString()
                )
            }
        })
        return@lazy story
    }

    fun getStory(id: String): LiveData<CatalogItem> {
        this.id = id
        return storyLiveData
    }
}