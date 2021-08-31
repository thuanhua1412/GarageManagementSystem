package com.example.centergaragemanagementsystem.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.centergaragemanagementsystem.dataClass.Customer
import com.example.centergaragemanagementsystem.databinding.CustomerItemBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class CustomerItemAdapter(options: FirestoreRecyclerOptions<Customer>)
    : FirestoreRecyclerAdapter<Customer, CustomerItemAdapter.CustomerItemViewHolder>(options){

    class CustomerItemViewHolder private constructor(val binding: CustomerItemBinding) : RecyclerView.ViewHolder(binding.root){
        companion object{
            fun from(parent: ViewGroup) : CustomerItemViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CustomerItemBinding.inflate(layoutInflater, parent, false)

                return CustomerItemViewHolder(binding)
            }
        }
        fun bind(item : Customer){
            Log.d("Binding Item: ", "$item")
            binding.customer = item
        }
    }

    override fun onBindViewHolder(holder: CustomerItemViewHolder, position: Int, model: Customer) {
        holder.bind(model)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerItemViewHolder {
        Log.d("CustomerItemViewHolder: ", "onCreatViewHoleder")
        return CustomerItemViewHolder.from(parent)
    }
}

