package com.womannotfound.odinia.views.ui.fragments.controls.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.womannotfound.odinia.R
import kotlinx.android.synthetic.main.activities_item.view.*

class ActivityAdapter(private val ActivityList:List<ActivitiesItems>): RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>(){

    override fun onCreateViewHolder(parent:ViewGroup, viewType: Int): ActivityViewHolder{
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activities_item,parent,false)
        return ActivityViewHolder(
            itemView
        )
    }

    override fun getItemCount() = ActivityList.size

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val currentItem = ActivityList[position]

        holder.activityName.text = currentItem.activityName
        holder.activityDate.text = currentItem.activityDate
        holder.activityAmount.text = currentItem.activityAmount
        holder.cardView.setCardBackgroundColor(currentItem.cardColor)
    }

    class ActivityViewHolder (ItemView: View): RecyclerView.ViewHolder(ItemView){
        val cardView: CardView = ItemView.card_color
        val activityName: TextView = ItemView.activity_name
        val activityDate: TextView = ItemView.activity_date
        val activityAmount: TextView = ItemView.activity_amount
    }
}
