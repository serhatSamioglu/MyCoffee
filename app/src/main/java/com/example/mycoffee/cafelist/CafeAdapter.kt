package com.example.mycoffee.cafelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mycoffee.R
import com.example.mycoffee.dataclass.CafeListItem

class CafeAdapter(private val cafeListItem : ArrayList<CafeListItem>): RecyclerView.Adapter<CafeAdapter.CafeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_cafe, parent, false)
        return CafeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CafeViewHolder, position: Int) {
        val currentItem = cafeListItem[position]

        holder.cafeName.text = currentItem.cafe?.name
        holder.giftCount.text = currentItem.star?.giftCount.toString()
    }

    override fun getItemCount(): Int {
        return cafeListItem.size
    }

    class CafeViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val cafeName : TextView = itemView.findViewById(R.id.cafeName)
        val giftCount : TextView = itemView.findViewById(R.id.giftCount)
    }
}