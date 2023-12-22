package com.tri.sulton.inigua.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.tri.sulton.inigua.data.api.model.Login
import com.tri.sulton.inigua.data.api.model.Register
import com.tri.sulton.inigua.data.api.model.response.CatalogItem
import com.tri.sulton.inigua.data.api.model.response.LoginResponse
import com.tri.sulton.inigua.data.api.retrofit.ApiConfig
import com.tri.sulton.inigua.data.api.retrofit.ApiService
import com.tri.sulton.inigua.data.database.IniGuaDatabase
import com.tri.sulton.inigua.data.pref.UserModel
import com.tri.sulton.inigua.data.pref.UserPreference
import com.tri.sulton.inigua.helper.Constant
import com.tri.sulton.inigua.helper.wrapEspressoIdlingResource
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.HttpException
import java.io.File

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val iniGuaDatabase: IniGuaDatabase,
    private var apiService: ApiService
) {

    private var token = ""

    fun register(data: Register): Call<LoginResponse> = apiService.register(
        username = data.username,
        password = data.password
    )

    fun login(data: Login) = wrapEspressoIdlingResource {
        apiService.login(
            username = data.username,
            password = data.password
        )
    }

    fun getProducts(): LiveData<PagingData<CatalogItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 8),
            remoteMediator = ProductRemoteMediator(iniGuaDatabase, apiService),
            pagingSourceFactory = {
                iniGuaDatabase.productDao().getAllCatalog()
            }
        ).liveData
    }

    fun getDetailProduct(id: String) = apiService.getDetailCatalog(id)

    fun getPantsRecommendation(color: String) = apiService.getPantsRecommendation(color)
    fun getClothingRecommendation(color: String) = apiService.getClothingRecommendation(color)

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun changeApiService(token: String) {
        apiService = ApiConfig.getApiService(token)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
        token = ""
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            database: IniGuaDatabase,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, database, apiService)
            }.also { instance = it }
    }
}