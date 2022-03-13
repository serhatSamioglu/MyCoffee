package com.example.mycoffee.cafelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mycoffee.R
import com.example.mycoffee.dataclass.CafeListItem
import com.squareup.picasso.Picasso

class CafeAdapter(private val cafeListItem : ArrayList<CafeListItem>): RecyclerView.Adapter<CafeAdapter.CafeViewHolder>() {

    private lateinit var mListener : onItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_cafe, parent, false)
        return CafeViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: CafeViewHolder, position: Int) {
        val currentItem = cafeListItem[position]

        Picasso.get().load(currentItem.cafe?.logo).fit().centerInside().into(holder.logo)// daha performanslı yapılabilir
        holder.cafeName.text = currentItem.cafe?.name
        holder.starCount.text = currentItem.reward?.starCount.toString()
        holder.requiredStar.text = currentItem.cafe?.requiredStar.toString()
        holder.giftCount.text = currentItem.reward?.giftCount.toString()
    }

    override fun getItemCount(): Int {
        return cafeListItem.size
    }

    class CafeViewHolder(itemView : View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
        val logo : ImageView = itemView.findViewById(R.id.logo)
        val cafeName : TextView = itemView.findViewById(R.id.cafeName)
        val starCount : TextView = itemView.findViewById(R.id.starCount)
        val requiredStar : TextView = itemView.findViewById(R.id.requiredStar)
        val giftCount : TextView = itemView.findViewById(R.id.giftCount)
    }

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }
}