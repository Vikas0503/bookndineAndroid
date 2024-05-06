package com.example.bookndineapplication.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookndineapplication.DataClasses.AddBookingData
import com.example.bookndineapplication.R
import com.squareup.picasso.Picasso

class BookedHistoryAdapter(private var bookedHistory:ArrayList<AddBookingData>):RecyclerView.Adapter<BookedHistoryAdapter.BookedHistoryViewHolder>() {
    class BookedHistoryViewHolder(view:View):RecyclerView.ViewHolder(view) {
        val restaurantNameTV = view.findViewById<TextView>(R.id.restaurantNameTV)
        val restaurantIV = view.findViewById<ImageView>(R.id.restaurantIV)
        val statusTV = view.findViewById<TextView>(R.id.statusTV)
        val timeTV = view.findViewById<TextView>(R.id.timeTV)
        val dateTV = view.findViewById<TextView>(R.id.dateTV)
        val noOfPersonsTV = view.findViewById<TextView>(R.id.noOfPersonsTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookedHistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.booked_history_adapter_layout,parent,false)
        return BookedHistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookedHistory.size
    }

    override fun onBindViewHolder(holder: BookedHistoryViewHolder, position: Int) {
        val history = bookedHistory[position]
        holder.restaurantNameTV.text = history.restaurantName
        holder.statusTV.text = "status:${history.status}"
        holder.timeTV.text = history.time
        holder.dateTV.text = history.date
        holder.noOfPersonsTV.text = history.noOfPersons
        Picasso.get().load(history.restaurantImage).into(holder.restaurantIV)
    }
}