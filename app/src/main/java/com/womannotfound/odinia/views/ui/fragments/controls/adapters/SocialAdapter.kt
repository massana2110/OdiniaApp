package com.womannotfound.odinia.views.ui.fragments.controls.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.womannotfound.odinia.R
import kotlinx.android.synthetic.main.social_items.view.*


class SocialAdapter(private val SocialList: List<SocialItems>, val clickListener:OnSocialItemClickListener) :
    RecyclerView.Adapter<SocialAdapter.SocialViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocialViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.social_items, parent, false)

        return SocialViewHolder(
            itemView
        )
    }

    override fun getItemCount() = SocialList.size

    override fun onBindViewHolder(holder: SocialViewHolder, position: Int) {
        holder.bindItems(SocialList[position],clickListener)

    }

    class SocialViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItems(data: SocialItems, action: OnSocialItemClickListener) {
            val imageView: ImageView = itemView.image_view_social
            val category: TextView = itemView.text_view_category
            val date: TextView = itemView.text_view_date
            val username: TextView = itemView.text_view_username
            val userImg: ImageView = itemView.image_view_user

            category.text = data.category
            date.text = data.date
            username.text = data.username

            itemView.setOnClickListener {
                action.onItemClick(data, adapterPosition)
            }

            Glide.with(itemView.context).load(data.imageResource).into(imageView)
            Glide.with(itemView.context).load(data.userImg).into(userImg)
        }

    }

    interface OnSocialItemClickListener {
        fun onItemClick(item: SocialItems, position: Int)
    }

}