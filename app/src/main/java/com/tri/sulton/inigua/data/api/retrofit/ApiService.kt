package com.tri.sulton.inigua.data.api.retrofit

import com.tri.sulton.inigua.data.api.model.response.CatalogItem
import com.tri.sulton.inigua.data.api.model.response.CommonResponse
import com.tri.sulton.inigua.data.api.model.response.LoginResponse
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

interface ApiService {
    @FormUrlEncoded
    @POST("user/register")
    fun register(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<CommonResponse<String>>

    @FormUrlEncoded
    @POST("user/login")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @Multipart
    @POST("fashion")
    suspend fun upload(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): CommonResponse<Nothing>

    @GET("catalog")
    suspend fun getAllCatalog(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): CommonResponse<List<CatalogItem>>

    @GET("catalog/{id}")
    fun getDetailCatalog(
        @Path("id") id: String): Call<CommonResponse<CatalogItem>>

    @GET("catalog/recommendation/pants/{color}")
    fun getPantsRecommendation(
        @Path("color") color: String): Call<CommonResponse<List<CatalogItem>>>

    @GET("catalog/recommendation/clothes/{color}")
    fun getClothingRecommendation(
        @Path("color") color: String): Call<CommonResponse<List<CatalogItem>>>

}