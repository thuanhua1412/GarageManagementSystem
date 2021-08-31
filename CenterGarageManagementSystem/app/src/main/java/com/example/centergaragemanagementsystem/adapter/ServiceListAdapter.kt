package com.example.centergaragemanagementsystem.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.centergaragemanagementsystem.R
import com.example.centergaragemanagementsystem.dataClass.*
import com.example.centergaragemanagementsystem.databinding.NhanXeCarItemBinding

class ServiceClickListener(val clickListener :(service: Service ) -> Unit){
    fun onClick(service: Service) = clickListener(service)
}

class ServiceListAdapter(private val serviceList: ArrayList<Service>, private val carList: ArrayList<Car>, private val clickListener: ServiceClickListener, private val confirmClick: ServiceClickListener, private val disable:Boolean = false) : RecyclerView.Adapter<ServiceListAdapter.ServiceItemViewHolder>(){
    class ServiceItemViewHolder private constructor(val parent:ViewGroup, val binding: NhanXeCarItemBinding,val carList: ArrayList<Car>, val serviceList: ArrayList<Service>) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Service, clickListener: ServiceClickListener, confirmClick: ServiceClickListener, disable: Boolean){
            binding.serviceItem = item
            binding.car = getCarFromService(item)
            binding.serviceStatus.setImageResource(getServiceStatusDrawableFromString(item.status))
            binding.clickListener = clickListener
            binding.confirmClick = confirmClick
            if (disable){
                binding.btnConfirm.visibility = View.GONE
                binding.btnCancle.visibility = View.GONE
            }
            val filteredServiceList = serviceList.filter {
                it.cartID == item.cartID
            }
            binding.spinner.adapter = ServiceItemSpinnerAdapter(parent.context, ArrayList(filteredServiceList))
//            binding.spinner.adapter = ServiceItemSpinnerAdapter()

        }

        private fun getCarFromService(item: Service): Car? {
            return carList.find{
                it.registrationPlate == item.carServiced
            }
        }

        private fun getServiceStatusDrawableFromString(status : String) : Int{
            return when(status){
                SERVICE_CONFIRMATION_WAITING -> R.drawable.status_waiting_approval
                SERVICE_WAITING -> R.drawable.status_not_started
                SERVICE_EXECUTING -> R.drawable.status_doing
                SERVICE_FINISHED -> R.drawable.status_done
                else -> R.drawable.status_waiting_approval
            }
        }
        companion object{
            fun from(parent: ViewGroup, carList: ArrayList<Car>, serviceList: ArrayList<Service>) : ServiceItemViewHolder{
                val layoutInflater  = LayoutInflater.from(parent.context)
                val binding = NhanXeCarItemBinding.inflate(layoutInflater,parent,false)

                return ServiceItemViewHolder(parent, binding,carList,serviceList)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceItemViewHolder {
        Log.d("Service Item Adapter", "Creating Service Item ViewHolder")
        return ServiceItemViewHolder.from(parent,carList,serviceList)
    }
//
//    override fun onBindViewHolder(holder: ServiceItemViewHolder, position: Int, model: Service) {
//        holder.bind(model)
//    }

    override fun onBindViewHolder(holder: ServiceItemViewHolder, position: Int) {
        holder.bind(serviceList.distinctBy {
            it.cartID
        }[position],clickListener, confirmClick,disable)
    }

    override fun getItemCount(): Int {
        return serviceList.distinctBy {
            it.cartID
        }.size
    }

}
