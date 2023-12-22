package com.tri.sulton.inigua.data.api.retrofit

import com.tri.sulton.inigua.data.api.model.response.CatalogItem
import com.tri.sulton.inigua.data.api.model.response.CommonResponse
import com.tri.sulton.inigua.data.api.model.response.LoginResponse
import com.tri.sulton.inigua.data.api.model.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface UploadService {
    @Multipart
    @POST("fashion")
    suspend fun upload(
        @Part file: MultipartBody.Part,
        @Part("username") username: RequestBody,
    ): UploadResponse


}