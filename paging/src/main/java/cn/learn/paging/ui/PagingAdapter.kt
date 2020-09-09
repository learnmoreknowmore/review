package cn.learn.paging.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import cn.learn.paging.R
import cn.learn.paging.vo.Post
import com.bumptech.glide.Glide

class PagingAdapter : PagingDataAdapter<Post, PagingAdapter.PagingViewHolder>(DIFF) {
    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }
        }
    }

    class PagingViewHolder(private val view:View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.title)
        private val subtitle: TextView = view.findViewById(R.id.subtitle)
        private val score: TextView = view.findViewById(R.id.score)
        private val thumbnail : ImageView = view.findViewById(R.id.thumbnail)
        private var post : Post? = null

        fun bindItem(item: Post?) {
            this.post = post
            title.text = post?.title ?: "loading"
            subtitle.text = post?.author
            score.text = "${post?.score ?: 0}"
            if (post?.thumbnail?.startsWith("http") == true) {
                thumbnail.visibility = View.VISIBLE
                Glide.with(view)
                    .load(post?.thumbnail)
                    .centerCrop()
                    //.placeholder(R.mipmap.ic_launcher_round)
                    .into(thumbnail)
            } else {
                thumbnail.visibility = View.GONE
            }
        }

    }

    override fun onBindViewHolder(holder: PagingViewHolder, position: Int) {
        holder.bindItem(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagingViewHolder {
       return PagingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_post,parent,false))
    }
}