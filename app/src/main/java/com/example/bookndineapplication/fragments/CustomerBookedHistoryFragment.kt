package com.example.bookndineapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookndineapplication.Adapters.BookedHistoryAdapter
import com.example.bookndineapplication.R
import com.example.bookndineapplication.databinding.FragmentCustomerBookedHistoryBinding

class CustomerBookedHistoryFragment : Fragment() {

    private lateinit var binding: FragmentCustomerBookedHistoryBinding
    private lateinit var adapter:BookedHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomerBookedHistoryBinding.inflate(layoutInflater)

        setBookedHistoryAdapter()

        return binding.root
    }

    private fun setBookedHistoryAdapter() {
        binding.bookedHistoryRV.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        adapter = BookedHistoryAdapter(ArrayList())
        binding.bookedHistoryRV.adapter = adapter
    }
}