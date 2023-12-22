package com.tri.sulton.inigua.data.api.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UploadResponse(

    @field:SerializedName("filename")
	val filename: String,

    @field:SerializedName("predict_image")
	val predict_image: String,

	@field:SerializedName("datetime")
	val datetime: String,

	@field:SerializedName("color_bottom")
	val color_bottom: String,

	@field:SerializedName("color_skin")
	val color_skin: String,

	@field:SerializedName("color_top")
	val color_top: String,

	@field:SerializedName("percentage_clothes_pants")
	val percentage_clothes_pants: Int,

	@field:SerializedName("percentage_skin_clothes")
	val percentage_skin_clothes: Int,

) : Parcelable