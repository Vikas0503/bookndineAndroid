package com.example.bookndineapplication.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookndineapplication.DataClasses.AddBookingData
import com.example.bookndineapplication.HelperClasses.SharedPreferences
import com.example.bookndineapplication.R

class OrdersListAdapter(private var ordersList:ArrayList<AddBookingData>,var context: Context):RecyclerView.Adapter<OrdersListAdapter.OrdersListviewHolder>() {

    private var monItemClickListeners:OnItemClickListeners ?= null
    class OrdersListviewHolder(view:View):RecyclerView.ViewHolder(view) {
        val bookedUserNameTV:TextView = view.findViewById(R.id.bookedUserNameTV)
        val noOfMembersTV:TextView = view.findViewById(R.id.noOfMembersTV)
        val bookedDateTV:TextView = view.findViewById(R.id.bookedDateTV)
        val acceptBTN:Button = view.findViewById(R.id.acceptBTN)
        val rejectBTN:Button = view.findViewById(R.id.rejectBTN)
    }

    interface OnItemClickListeners {
        fun onRejectClick(ordersList: AddBookingData,position: Int)
        fun onAcceptClick(ordersList: AddBookingData,position: Int)
    }

    fun setOnClickListeners(onItemClick:OnItemClickListeners) {
        monItemClickListeners = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersListviewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.orders_list_adapter_layout, parent, false)
        return OrdersListviewHolder(view)
    }

    override fun getItemCount(): Int {
        SharedPreferences(context).saveOrdersCount(ordersList.size)
        return ordersList.size
    }

    override fun onBindViewHolder(holder: OrdersListviewHolder, position: Int) {
        val orderList = ordersList[position]
        holder.bookedUserNameTV.text = orderList.userName
        holder.noOfMembersTV.text = orderList.noOfPersons
        holder.bookedDateTV.text = orderList.date
        holder.acceptBTN.setOnClickListener {
            monItemClickListeners?.onAcceptClick(orderList,position)
        }
        holder.rejectBTN.setOnClickListener {
            monItemClickListeners?.onRejectClick(orderList,position)
        }
    }
}
