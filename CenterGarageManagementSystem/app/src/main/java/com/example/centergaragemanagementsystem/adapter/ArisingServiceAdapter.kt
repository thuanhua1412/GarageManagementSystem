package com.example.centergaragemanagementsystem.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.centergaragemanagementsystem.dataClass.ServiceItem
import com.example.centergaragemanagementsystem.databinding.ArisingServiceItemBinding

class ArisingServiceClickListener(val clickListener :(position:Int) -> Unit){
    fun onClick(position: Int) = clickListener(position)
}

class ArisingServiceAdapter(private var serviceItemList:ArrayList<ServiceItem>, private val clickListener: ArisingServiceClickListener) : RecyclerView.Adapter<ArisingServiceAdapter.ArisingServiceViewHolder>(){
    class ArisingServiceViewHolder private constructor(val binding: ArisingServiceItemBinding, val serviceItemList: ArrayList<ServiceItem>) : RecyclerView.ViewHolder(binding.root) {
        fun bind(serviceItem: ServiceItem, clickListenter: ArisingServiceClickListener){
            binding.serviceItem = serviceItem
            binding.clickListener = clickListenter
        }

        companion object{
            fun from(parent: ViewGroup, serviceItemList: ArrayList<ServiceItem>) : ArisingServiceViewHolder {
                val layoutInflater  = LayoutInflater.from(parent.context)
                val binding = ArisingServiceItemBinding.inflate(layoutInflater,parent,false)

                return ArisingServiceViewHolder(binding, serviceItemList)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArisingServiceViewHolder {
        Log.d("Arising Service Adapter", "Creating Arising Service Item ViewHolder")
        return ArisingServiceViewHolder.from(parent, serviceItemList)
    }

    override fun onBindViewHolder(holder: ArisingServiceViewHolder, position: Int) {
        holder.bind(serviceItemList[position], clickListener)
        holder.binding.position = holder.absoluteAdapterPosition
    }
    override fun getItemCount(): Int {
        return serviceItemList.size
    }

    fun removeAt(position:Int){
        serviceItemList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, serviceItemList.size)
    }
}