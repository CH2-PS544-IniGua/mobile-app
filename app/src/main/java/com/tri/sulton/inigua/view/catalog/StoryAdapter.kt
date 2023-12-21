package com.tri.sulton.inigua.view.catalog

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tri.sulton.inigua.R
import com.tri.sulton.inigua.data.api.model.response.CatalogItem
import com.tri.sulton.inigua.databinding.ItemStoryBinding
import com.tri.sulton.inigua.helper.Constant.currency
import com.tri.sulton.inigua.helper.Constant.hexOf

class StoryAdapter : PagingDataAdapter<CatalogItem, StoryAdapter.ListViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
            holder.itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(item)
            }
        }
    }

    class ListViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CatalogItem) {
            binding.name.text = item.title
            Glide.with(itemView.context)
                .load(item.image)
                .into(binding.image)
            binding.coloredBar.setContent {
                MaterialTheme {
                    Box(
                        modifier = Modifier.background(Color(hexOf(item.color)))
                    )
                }
            }
            binding.price.text = currency(item.price)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(item: CatalogItem)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CatalogItem>() {
            override fun areItemsTheSame(oldItem: CatalogItem, newItem: CatalogItem): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: CatalogItem, newItem: CatalogItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
