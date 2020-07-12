package com.womannotfound.odinia.views.ui.fragments.controls.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.womannotfound.odinia.R
import kotlinx.android.synthetic.main.payments_item.view.*

class PaymentAdapter (options: FirestoreRecyclerOptions<PaymentsItems>): FirestoreRecyclerAdapter<PaymentsItems, PaymentAdapter.PaymentViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.payments_item,parent,false)
        return PaymentViewHolder(
            itemView
        )
    }

    class PaymentViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.image_view_accounts
        val textView: TextView = itemView.txt_view_accounts
        val textView2: TextView = itemView.text_view2_accounts
        val textView3: TextView = itemView.text_view3
        val textView4: TextView = itemView.text_view4_accounts
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int, model: PaymentsItems) {
        holder.imageView.setImageResource(model.imageResource)
        holder.textView.text = model.namePayment
        holder.textView2.text = model.categoryPayment
        holder.textView3.text = model.datePayment
        holder.textView4.text = "$${model.amountPayment}"
    }
}