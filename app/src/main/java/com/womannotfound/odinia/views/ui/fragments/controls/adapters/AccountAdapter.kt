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

class AccountAdapter (private val AccountsList:List<AccountsItems>): RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.accounts_item,parent,false)
        return AccountViewHolder(
            itemView
        )
    }

    override fun getItemCount() = AccountsList.size

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val currentItem = AccountsList[position]

        holder.imageView.setImageResource(currentItem.imageResource)
        holder.textView.text = currentItem.name
        holder.textView1.text = currentItem.balance
        holder.textView2.text = currentItem.type

    }

    class AccountViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.image_view_accounts
        val textView1: TextView = itemView.text_view4_accounts
        val textView: TextView = itemView.txt_view_accounts
        val textView2: TextView = itemView.text_view5_accounts
    }

}