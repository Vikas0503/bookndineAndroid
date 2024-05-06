package com.example.bookndineapplication.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookndineapplication.DataClasses.AddRestaurantData
import com.example.bookndineapplication.R
import com.squareup.picasso.Picasso

class RestaurantListAdapter(private var restaurantList:ArrayList<AddRestaurantData>): RecyclerView.Adapter<RestaurantListAdapter.RestaurantListViewHolder>() {
    private var monItemClickListeners: OnItemClickListeners?= null

    interface OnItemClickListeners {
        fun onClickDetails(restaurant:AddRestaurantData,position: Int)
    }

    fun setOnClickListeners(onItemClick: OnItemClickListeners) {
        monItemClickListeners = onItemClick
    }

    class RestaurantListViewHolder(view:View):RecyclerView.ViewHolder(view) {
        val restaurantIV:ImageView = view.findViewById(R.id.restaurantIV)
        val restaurantNameTV:TextView = view.findViewById(R.id.restaurantNameTV)
        val restaurantAddressTV:TextView = view.findViewById(R.id.restaurantAddressTV)
        val viewDetailsBTN:Button = view.findViewById(R.id.viewDetailsBTN)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.restaurant_list_adapter_layout, parent, false)
        return RestaurantListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    override fun onBindViewHolder(holder: RestaurantListViewHolder, position: Int) {
        val restaurant = restaurantList[position]
        holder.restaurantNameTV.text = restaurant.restaurantName
        holder.restaurantAddressTV.text = restaurant.restaurantAddress
        Picasso.get().load(restaurant.imageUrl).placeholder(R.drawable.ic_launcher_foreground).into(holder.restaurantIV)
        holder.viewDetailsBTN.setOnClickListener {
            monItemClickListeners?.onClickDetails(restaurant,position)
        }
    }
}