package com.shivam.newsappshorts.fragments.bookmarks.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shivam.newsappshorts.databinding.ItemHomeRecyclerviewBinding
import com.shivam.newsappshorts.fragments.home.model.Article
import kotlin.collections.toMutableList

class BookmarkAdapter(private val listener: Listener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var flattenedList = mutableListOf<Article>()
    private lateinit var context: Context

    fun updateData(context: Context, newDataList: List<Article>?, key: String?) {

        val oldList = flattenedList
        val diffUtil: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            HomeItemDiffCallback(
                oldList, newDataList
            )
        )
        if (newDataList != null) {
            flattenedList = newDataList.toMutableList()
        }
        diffUtil.dispatchUpdatesTo(this)
    }

    class HomeItemDiffCallback(
        private var oldHomeList: MutableList<Article>,
        private var newHomeList: List<Article>?
    ) : androidx.recyclerview.widget.DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldHomeList.size
        }

        override fun getNewListSize(): Int {
            return newHomeList?.size ?: 0
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldHomeList.get(oldItemPosition).title == newHomeList?.get(newItemPosition)?.title)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return ((oldHomeList[oldItemPosition] == (newHomeList?.get(newItemPosition) ?: 0)))
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        this.context=parent.context
        val binding = ItemHomeRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHomeViewHolder(binding)

    }

    inner class ItemHomeViewHolder(val binding: ItemHomeRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return flattenedList.size

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = flattenedList[position]

        when (holder) {
            is ItemHomeViewHolder -> {

                holder.binding.tvPoliticsHeader.text="Saved"

                holder.binding.tvPoliticsSubHeader.text = item?.title
                context?.let { Glide.with(it).load(item?.urlToImage).into(holder.binding.imgShapeAble) }
                holder.binding.tvDate.text=item.publishedAt

                holder.binding.root.setOnClickListener {
                    listener.onItemClick(item)
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

    interface Listener {
        fun onItemBookmarkClick(selectedPosition: Int, ivBookmark: ImageView)
        fun isBookMark(selectedPosition: Int, ivBookmark: ImageView)
        fun onItemClick(item: Article)
        fun onItemShareClick(item: Article?)
    }

}