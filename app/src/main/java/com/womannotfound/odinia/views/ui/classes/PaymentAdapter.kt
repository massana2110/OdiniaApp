package com.womannotfound.odinia.views.ui.classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.womannotfound.odinia.R
import kotlinx.android.synthetic.main.payments_item.view.*

class PaymentAdapter (private val PaymentsList:List<PaymentsItems>):RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.payments_item,parent,false)
        return PaymentViewHolder(itemView)
    }

    override fun getItemCount() = PaymentsList.size

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        val currentItem = PaymentsList[position]

        holder.imageView.setImageResource(currentItem.imageResource)
        holder.textView.text = currentItem.name
        holder.textView2.text = currentItem.category
        holder.textView3.text = currentItem.date
        holder.textView4.text = currentItem.amount
    }

    class PaymentViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.image_view
        val textView: TextView = itemView.txt_view
        val textView2: TextView = itemView.text_view2
        val textView3: TextView = itemView.text_view3
        val textView4: TextView = itemView.text_view4
    }
}