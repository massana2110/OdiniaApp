package com.womannotfound.odinia.views.ui.fragments.controls.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.womannotfound.odinia.R
import kotlinx.android.synthetic.main.social_items.view.*

class SocialAdapter(private val SocialList: List<SocialItems>) :
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
        holder.bindItems(SocialList[position])

    }

    class SocialViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItems(data: SocialItems) {
            val imageView: ImageView = itemView.image_view_social
            val category: TextView = itemView.text_view_category
            val date: TextView = itemView.text_view_date

            category.text=data.category
            date.text=data.date

            Glide.with(itemView.context).load(data.imageResource).into(imageView)
        }

    }

}