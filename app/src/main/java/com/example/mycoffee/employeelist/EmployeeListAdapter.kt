package com.example.mycoffee.employeelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mycoffee.R
import com.example.mycoffee.dataclass.Employee

class EmployeeListAdapter(private val employeeList : ArrayList<Employee>): RecyclerView.Adapter<EmployeeListAdapter.EmployeeViewHolder>() {
    private lateinit var mListener : onItemLongClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_employee, parent, false)
        return EmployeeViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val currentItem = employeeList[position]

        holder.employeeName.text = currentItem.fullName
    }

    override fun getItemCount(): Int {
        return employeeList.size
    }

    class EmployeeViewHolder(itemView : View, listener: onItemLongClickListener) : RecyclerView.ViewHolder(itemView){
        init {
            itemView.setOnLongClickListener {
                listener.onItemLongClick(adapterPosition)
                true
            }
        }
        val employeeName : TextView = itemView.findViewById(R.id.employeeName)
    }

    interface onItemLongClickListener{
        fun onItemLongClick(position: Int)
    }

    fun setOnItemLongClickListener(listener: onItemLongClickListener) {
        mListener = listener
    }
}