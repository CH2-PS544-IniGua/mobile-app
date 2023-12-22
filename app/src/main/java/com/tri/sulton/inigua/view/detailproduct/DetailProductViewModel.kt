package com.tri.sulton.inigua.view.detailproduct

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

class DetailProductViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorResponse = MutableLiveData<ErrorResponse>()
    val errorResponse: LiveData<ErrorResponse> = _errorResponse

    var id = ""

    private val _pantsRecommendation = MutableLiveData<List<CatalogItem>>()
    val pantsRecommendation: LiveData<List<CatalogItem>> = _pantsRecommendation

    private val _clothingRecommendation = MutableLiveData<List<CatalogItem>>()
    val clothingRecommendation: LiveData<List<CatalogItem>> = _clothingRecommendation

    fun getPantsRecommendation(color: String) {
        val client = repository.getPantsRecommendation(color)
        client.enqueue(object : Callback<CommonResponse<List<CatalogItem>>> {
            override fun onResponse(
                call: Call<CommonResponse<List<CatalogItem>>>,
                response: Response<CommonResponse<List<CatalogItem>>>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _pantsRecommendation.value = responseBody.data
                } else {
                    _errorResponse.value = Constant.getErrorResponse(response.errorBody()!!.string())
                }
            }

            override fun onFailure(call: Call<CommonResponse<List<CatalogItem>>>, t: Throwable) {
                _errorResponse.value = ErrorResponse(
                    status = "error",
                    message = t.message.toString()
                )
            }
        })
    }

    fun getClothingRecommendation(color: String) {
        val client = repository.getClothingRecommendation(color)
        client.enqueue(object : Callback<CommonResponse<List<CatalogItem>>> {
            override fun onResponse(
                call: Call<CommonResponse<List<CatalogItem>>>,
                response: Response<CommonResponse<List<CatalogItem>>>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _clothingRecommendation.value = responseBody.data
                } else {
                    _errorResponse.value = Constant.getErrorResponse(response.errorBody()!!.string())
                }
            }

            override fun onFailure(call: Call<CommonResponse<List<CatalogItem>>>, t: Throwable) {
                _errorResponse.value = ErrorResponse(
                    status = "error",
                    message = t.message.toString()
                )
            }
        })
    }

    private val productLiveData: LiveData<CatalogItem> by lazy {
        val item = MutableLiveData<CatalogItem>()
        _isLoading.value = true
        val client = repository.getDetailProduct(id)
        client.enqueue(object : Callback<CommonResponse<CatalogItem>> {
            override fun onResponse(
                call: Call<CommonResponse<CatalogItem>>,
                response: Response<CommonResponse<CatalogItem>>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    item.value = responseBody.data

                } else {
                    _errorResponse.value = Constant.getErrorResponse(response.errorBody()!!.string())
                }
            }

            override fun onFailure(call: Call<CommonResponse<CatalogItem>>, t: Throwable) {
                _isLoading.value = false
                _errorResponse.value = ErrorResponse(
                    status = "error",
                    message = t.message.toString()
                )
            }
        })
        return@lazy item
    }

    fun getProduct(id: String): LiveData<CatalogItem> {
        this.id = id
        return productLiveData
    }
}