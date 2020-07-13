package com.womannotfound.odinia.views.ui.fragments.controls.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.womannotfound.odinia.R
import kotlinx.android.synthetic.main.activities_item.view.*

class ActivityAdapter(options: FirestoreRecyclerOptions<ActivitiesItems>):
    FirestoreRecyclerAdapter<ActivitiesItems, ActivityAdapter.ActivityViewHolder>(options) {

    override fun onCreateViewHolder(parent:ViewGroup, viewType: Int): ActivityViewHolder{
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activities_item,parent,false)
        return ActivityViewHolder(
            itemView
        )
    }

    class ActivityViewHolder (ItemView: View): RecyclerView.ViewHolder(ItemView){
        val cardView: CardView = ItemView.card_color
        val activityName: TextView = ItemView.activity_name
        val activityDate: TextView = ItemView.activity_date
        val activityAmount: TextView = ItemView.activity_amount
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int, model: ActivitiesItems) {
        holder.activityName.text = model.activityName
        holder.activityDate.text = model.activityDate
        holder.activityAmount.text = "$${model.activityAmount}"
        holder.cardView.setCardBackgroundColor(Color.parseColor(model.cardColor))
    }
}
