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
import retrofit2.HttpException
import java.io.File

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val iniGuaDatabase: IniGuaDatabase,
    private var apiService: ApiService
) {

    private var token = ""

    fun register(data: Register) = apiService.register(
        username = data.username,
        password = data.password
    )

    fun login(data: Login) = wrapEspressoIdlingResource {
        apiService.login(
            username = data.username,
            password = data.password
        )
    }

    fun getStories(): LiveData<PagingData<CatalogItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 6),
            remoteMediator = StoryRemoteMediator(iniGuaDatabase, apiService),
            pagingSourceFactory = {
                iniGuaDatabase.storyDao().getAllCatalog()
            }
        ).liveData
    }

    fun getDetailStory(id: String) = apiService.getDetailCatalog(id)

    fun uploadImage(imageFile: File, description: String) = liveData {
        emit(ResultState.Loading)

        wrapEspressoIdlingResource {
            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            try {
                val successResponse = apiService.upload(multipartBody, requestBody)
                emit(ResultState.Success(successResponse))
            } catch (e: HttpException) {
                val errorResponse = Constant.getErrorResponse(e.response()?.errorBody()?.string()!!)
                emit(ResultState.Error(errorResponse.message))
            }
        }
    }

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