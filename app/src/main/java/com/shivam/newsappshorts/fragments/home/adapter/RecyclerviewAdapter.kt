package com.shivam.newsappshorts.fragments.home.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shivam.newsappshorts.databinding.ItemHomeRecyclerviewBinding
import com.shivam.newsappshorts.fragments.home.model.Article
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class RecyclerviewAdapter(private val listener:Listener):PagingDataAdapter<Article,RecyclerView.ViewHolder>(Diff()){
    private var context: Context?=null
    private var section: String=""


    class Diff: DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.title==newItem.title
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem==newItem
        }
    }

    fun setSection(section: String){
        this.section=section

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        this.context=parent.context
        val binding =ItemHomeRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemHomeViewHolder(binding)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when (holder) {
            is ItemHomeViewHolder -> {

                holder.binding.tvPoliticsHeader.text=section

                holder.binding.tvPoliticsSubHeader.text = item?.title
                context?.let { Glide.with(it).load(item?.urlToImage).into(holder.binding.imgShapeAble) }
                holder.binding.tvDate.text=convertDateFormat(item?.publishedAt.toString())

                holder.binding.root.setOnClickListener {
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }

                holder.binding.share.setOnClickListener{
                    listener.onItemShareClick(item)
                }

                holder.binding.ivBookmark.setOnClickListener {

                    listener.onItemBookmarkClick(
                        holder.absoluteAdapterPosition,
                        holder.binding.ivBookmark
                    )

                }

                listener.isBookMark(
                    holder.absoluteAdapterPosition,
                    holder.binding.ivBookmark
                )


            }
        }

    }

    fun convertDateFormat(dateStr: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) // Example: "26 Feb 2025"
        val date = inputFormat.parse(dateStr)
        return date?.let { outputFormat.format(it) } ?: ""
    }

    interface Listener{
        fun onItemBookmarkClick(selectedPosition: Int, ivBookmark: ImageView)
        fun isBookMark(selectedPosition: Int, ivBookmark: ImageView)
        fun onItemClick(item: Article)
        fun onItemShareClick(item: Article?)
    }

    inner class ItemHomeViewHolder(val binding: ItemHomeRecyclerviewBinding):
        RecyclerView.ViewHolder(binding.root)

}

