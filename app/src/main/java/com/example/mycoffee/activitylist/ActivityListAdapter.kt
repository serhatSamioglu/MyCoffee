package com.example.mycoffee.activitylist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mycoffee.R
import com.example.mycoffee.dataclass.ActivityObject

class ActivityListAdapter (private val activityList : ArrayList<ActivityObject>, private val context : ActivityListActivity): RecyclerView.Adapter<ActivityListAdapter.ActivityListViewHolder>() {

    private lateinit var mListener : onItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_activity, parent, false)
        return ActivityListViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ActivityListViewHolder, position: Int) {
        val currentItem = activityList[position]

        holder.activityType.text = currentItem.type
        holder.customerName.text = currentItem.customerName
        holder.cashierName.text = currentItem.cashierName
        holder.activityDate.text = currentItem.formattedDate
    }

    override fun getItemCount(): Int {
        return activityList.size
    }

    class ActivityListViewHolder(itemView : View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
        val activityType : TextView = itemView.findViewById(R.id.activityType)
        val customerName : TextView = itemView.findViewById(R.id.customerName)
        val cashierName : TextView = itemView.findViewById(R.id.cashierName)
        val activityDate : TextView = itemView.findViewById(R.id.activityDate)
    }

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }
}