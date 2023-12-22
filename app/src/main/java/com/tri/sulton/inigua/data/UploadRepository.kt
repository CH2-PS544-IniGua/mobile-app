package com.tri.sulton.inigua.data

import androidx.lifecycle.liveData
import com.tri.sulton.inigua.data.api.retrofit.UploadService
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

class UploadRepository private constructor(
    private val userPreference: UserPreference,
    private val iniGuaDatabase: IniGuaDatabase,
    private var uploadService: UploadService
) {

    fun uploadImage(imageFile: File, username: String) = liveData {
        emit(ResultState.Loading)

        wrapEspressoIdlingResource {
            val requestBody = username.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "picture",
                imageFile.name,
                requestImageFile
            )
            try {
                val successResponse = uploadService.upload(multipartBody, requestBody)
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

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    companion object {
        @Volatile
        private var instance: UploadRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            database: IniGuaDatabase,
            uploadService: UploadService
        ): UploadRepository =
            instance ?: synchronized(this) {
                instance ?: UploadRepository(userPreference, database, uploadService)
            }.also { instance = it }
    }
}