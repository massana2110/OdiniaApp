package com.womannotfound.odinia.views.ui.fragments.controls.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.womannotfound.odinia.R

class HelpSlideAdapter(private val helpSlide: List<HelpSlide>) :
    RecyclerView.Adapter<HelpSlideAdapter.IntroSlideViewHolder>() {

    inner class IntroSlideViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textTitle = view.findViewById<TextView>(R.id.titleSlider)
        private val textDescription = view.findViewById<TextView>(R.id.textDescriptionSlider)
        private val image = view.findViewById<ImageView>(R.id.imageSlideIcon)

        fun bind(helpSlide: HelpSlide) {
            textTitle.text = helpSlide.title
            textDescription.text = helpSlide.description
            image.setImageResource(helpSlide.icon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroSlideViewHolder {
        return IntroSlideViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.slide_item_container,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return helpSlide.size
    }

    override fun onBindViewHolder(holder: IntroSlideViewHolder, position: Int) {
        holder.bind(helpSlide[position])
    }
}