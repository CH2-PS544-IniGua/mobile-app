package com.tri.sulton.inigua.helper

import com.google.gson.Gson
import com.tri.sulton.inigua.data.api.model.response.ErrorResponse
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale

object Constant {
    fun getErrorResponse(response: String): ErrorResponse {
        return Gson().fromJson(response, ErrorResponse::class.java)
    }

    fun hexOf(name: String): Long =
        when (name) {
            "Black" -> 0xFF000000
            "Blue" -> 0xFF1E90FF
            "Brown" -> 0xFF8B4513
            "Gray" -> 0xFFC0C0C0
            "Green" -> 0xFF7CFC00
            "Orange" -> 0xFFFFA500
            "Pink" -> 0xFFFFC0CB
            "Purple" -> 0xFF800080
            "Red" -> 0xFFFF0000
            "White" -> 0xFFFFFAFA
            "Yellow" -> 0xFFFFFF00
            "Cream" -> 0xFFFFFDD0
            else -> 0xFFFFFFFF
        }

    fun currency(number: Int): String {
        val decimalFormat = DecimalFormat("#,###")
        return "Rp" + decimalFormat.format(number.toLong()).replace(",", ".")
    }
}