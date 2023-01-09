package com.iqbal.trashbank.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iqbal.trashbank.R
import com.iqbal.trashbank.model.ResponseListJP
import kotlinx.android.synthetic.main.item_carry.view.*

class AdapterUserListJP(val list: ArrayList<ResponseListJP>):RecyclerView.Adapter<AdapterUserListJP.ViewHolder>() {

    inner class ViewHolder (itemview: View):RecyclerView.ViewHolder(itemview) {
        fun bind(role : ResponseListJP) {
            with(itemView) {
                itemView.itm_tanggaltransaksi.text = role.tanggal
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.item_carry,parent,false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size


}

