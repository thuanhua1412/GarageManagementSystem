package com.example.centergaragemanagementsystem.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.centergaragemanagementsystem.R
import com.example.centergaragemanagementsystem.dataClass.*
import com.example.centergaragemanagementsystem.databinding.ActivityCarinfoBinding
import com.example.centergaragemanagementsystem.databinding.ServiceItemThinBinding
import com.example.centergaragemanagementsystem.viewmodel.ServiceViewModel

//class ServiceClickListener(val clickListener :(service: Service ) -> Unit){
//    fun onClick(service: Service) = clickListener(service)
//}

class ServiceItemAdapter(private val serviceList: ArrayList<Service>, private val carList: ArrayList<Car>, private val clickListener: ServiceClickListener, private val viewModel: ServiceViewModel) : RecyclerView.Adapter<ServiceItemAdapter.ServiceItemViewHolder>(){
    class ServiceItemViewHolder private constructor(val parent:ViewGroup, val binding: ServiceItemThinBinding, val bindingcar: ActivityCarinfoBinding ,val carList: ArrayList<Car>, val serviceList: ArrayList<Service>, val viewModel: ServiceViewModel) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Service, clickListener: ServiceClickListener){
            binding.service = item
            bindingcar.car = getCarFromService(item)

            binding.statusImage.setImageResource(getServiceStatusDrawableFromString(item.status))


            binding.clickListener = clickListener


//            binding.spinner.adapter = ServiceItemSpinnerAdapter(parent.context, ArrayList(filteredServiceList))
////            binding.spinner.adapter = ServiceItemSpinnerAdapter()

        }
        private fun getService(item : Car): Service ?{
            return serviceList.find{
                it.ownerID == item.ownerID
            }
        }
        private fun getCarFromService(item: Service): Car? {
            return carList.find{
                it.registrationPlate == item.carServiced
                it.ownerID == item.ownerID
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
            fun from(parent: ViewGroup, carList: ArrayList<Car>, serviceList: ArrayList<Service>, viewModel: ServiceViewModel) : ServiceItemViewHolder{
                val layoutInflater  = LayoutInflater.from(parent.context)
                val binding = ServiceItemThinBinding.inflate(layoutInflater,parent,false)
                val bindingcar = ActivityCarinfoBinding.inflate(layoutInflater, parent, false)
                return ServiceItemViewHolder(parent, binding, bindingcar  ,carList,serviceList, viewModel)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceItemViewHolder {
        Log.d("Service Item Adapter", "Creating Service Item ViewHolder")
        return ServiceItemViewHolder.from(parent,carList,serviceList, viewModel)
    }
//
//    override fun onBindViewHolder(holder: ServiceItemViewHolder, position: Int, model: Service) {
//        holder.bind(model)
//    }

    override fun onBindViewHolder(holder: ServiceItemViewHolder, position: Int) {
        holder.bind(serviceList[position],clickListener)
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

}

