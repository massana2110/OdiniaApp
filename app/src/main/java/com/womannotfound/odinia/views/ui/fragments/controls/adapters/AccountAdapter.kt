package com.womannotfound.odinia.views.ui.fragments.controls.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.womannotfound.odinia.R
import kotlinx.android.synthetic.main.accounts_item.view.*
import kotlinx.android.synthetic.main.payments_item.view.image_view_accounts
import kotlinx.android.synthetic.main.payments_item.view.text_view4_accounts
import kotlinx.android.synthetic.main.payments_item.view.txt_view_accounts

import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class AccountAdapter (options: FirestoreRecyclerOptions<AccountsItems>):
    FirestoreRecyclerAdapter<AccountsItems, AccountAdapter.AccountViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.accounts_item,parent,false)
        return AccountViewHolder(
            itemView
        )
    }

    class AccountViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.image_view_accounts
        val textView1: TextView = itemView.text_view4_accounts
        val textView: TextView = itemView.txt_view_accounts
        val textView2: TextView = itemView.text_view5_accounts
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int, model: AccountsItems) {
        holder.imageView.setImageResource(model.imageResource)
        holder.textView.text = model.nameAccount
        holder.textView1.text = "$${model.balanceAccount}"
        holder.textView2.text = model.typeAccount
    }

}