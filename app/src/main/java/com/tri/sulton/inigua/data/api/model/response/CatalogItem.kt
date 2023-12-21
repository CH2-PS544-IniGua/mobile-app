package com.tri.sulton.inigua.data.api.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "catalog")
data class CatalogItem(
	@PrimaryKey
	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("overview")
	val overview: String,

	@field:SerializedName("color")
	val color: String,

	@field:SerializedName("material")
	val material: String,

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("short_desc")
	val short_desc: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("type")
	val type: String,


)
