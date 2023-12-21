package com.tri.sulton.inigua.helper

import com.google.gson.Gson
import com.tri.sulton.inigua.data.api.model.response.ErrorResponse

object Constant {
    fun getErrorResponse(response: String): ErrorResponse {
        return Gson().fromJson(response, ErrorResponse::class.java)
    }
}