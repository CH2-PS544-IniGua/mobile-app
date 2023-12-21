package com.tri.sulton.inigua.data.api.model.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("status")
	val status: String,

    @field:SerializedName("message")
	val message: String,

	@field:SerializedName("data")
	val data: String,

	@field:SerializedName("username")
	val username: String,

)