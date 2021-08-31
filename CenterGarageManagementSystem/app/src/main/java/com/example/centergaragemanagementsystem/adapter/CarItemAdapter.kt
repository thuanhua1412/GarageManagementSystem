package com.example.centergaragemanagementsystem.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.centergaragemanagementsystem.R
import com.example.centergaragemanagementsystem.viewmodel.ServiceViewModel
import com.example.centergaragemanagementsystem.dataClass.*
import com.example.centergaragemanagementsystem.databinding.CarItemBinding

//class ServiceClickListener(val clickListener :(service: Service ) -> Unit){
//    fun onClick(service: Service) = clickListener(service)
//}

class CarItemAdapter(private val serviceList: ArrayList<Service>, private val carList: ArrayList<Car>, private val clickListener: ServiceClickListener, private val viewModel: ServiceViewModel) : RecyclerView.Adapter<CarItemAdapter.ServiceItemViewHolder>(){
    class ServiceItemViewHolder private constructor(val parent:ViewGroup, val binding: CarItemBinding,val carList: ArrayList<Car>, val serviceList: ArrayList<Service>, val viewModel: ServiceViewModel) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Service, clickListener: ServiceClickListener){
            binding.serviceItem = item
            binding.car = getCarFromService(item)
            binding.statusImage.setImageResource(getServiceStatusDrawableFromString(item.status))
            binding.clickListener = clickListener


            val filteredServiceList = serviceList.filter {
                it.cartID == item.cartID
            }

        }

        private fun getCarFromService(item: Service): Car? {
            return carList.find{
//                it.registrationPlate == item.carServiced
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
                val binding = CarItemBinding.inflate(layoutInflater,parent,false)

                return ServiceItemViewHolder(parent, binding,carList,serviceList, viewModel)
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
        holder.bind(serviceList.distinctBy {
            it.cartID
        }[position],clickListener)
    }

    override fun getItemCount(): Int {
        return serviceList.distinctBy {
            it.cartID
        }.size
    }

}



//package com.example.garagemanagementsystem.adapter
//
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.recyclerview.widget.RecyclerView
//import com.example.garagemanagementsystem.R
//import com.example.garagemanagementsystem.dataClass.*
//import com.example.garagemanagementsystem.databinding.CarItemBinding
//import com.firebase.ui.firestore.FirestoreRecyclerAdapter
//import com.firebase.ui.firestore.FirestoreRecyclerOptions
//
//class CarItemAdapter(options: FirestoreRecyclerOptions<Car>)
//    : FirestoreRecyclerAdapter<Car, CarItemAdapter.CarItemViewHolder>(options){
//
//    class CarItemViewHolder private constructor(val binding: CarItemBinding) : RecyclerView.ViewHolder(binding.root){
//        companion object{
//            fun from(parent: ViewGroup) : CarItemViewHolder{
//                val layoutInflater = LayoutInflater.from(parent.context)
//                val binding = CarItemBinding.inflate(layoutInflater, parent, false)
//
//                return CarItemViewHolder(binding)
//            }
//        }
//        init {
//            itemView.setOnClickListener{
//                val pos: Int = adapterPosition
//                Toast.makeText(itemView.context, "Clicked on ${pos}", Toast.LENGTH_SHORT)
//            }
//        }
//        val serviceList : ArrayList<Service> =
//        fun bind(item : Car, ser : Service){
//            Log.d("Binding Item: ", "$item")
//            binding.car = item
//            binding.statusImage.setImageResource(getServiceStatusDrawableFromString(ser.status))
//            //binding.service = getServiceFromCar(item)
//
//
//        }
//        private fun getServiceStatusDrawableFromString(status : String) : Int{
//            return when(status){
//                SERVICE_CONFIRMATION_WAITING -> R.drawable.status_waiting_approval
//                SERVICE_WAITING -> R.drawable.status_not_started
//                SERVICE_EXECUTING -> R.drawable.status_doing
//                SERVICE_FINISHED -> R.drawable.status_done
//                else -> R.drawable.status_waiting_approval
//            }
//        }
////        private fun getServiceFromCar(item: Car): Service ?{
////            return serviceList.find {
////                it.ownerID == item.ownerID
////            }
////        }
//    }
//
//    override fun onBindViewHolder(holder: CarItemViewHolder, position: Int, model: Car) {
//        holder.bind(model)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarItemViewHolder {
//        Log.d("CustomerItemViewHolder: ", "onCreatViewHoleder")
//        return CarItemViewHolder.from(parent)
//    }
//
//}









//package com.example.garagemanagementsystem.adapter
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.garagemanagementsystem.Item.CarItem
//import com.example.garagemanagementsystem.R
//
//class CarItemAdapter(private val carList: List<CarItem>,
//                     private val listener : OnItemClickListener
//) : RecyclerView.Adapter<CarItemAdapter.CarItemViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarItemViewHolder {
//        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.car_item, parent, false)
//
//        return CarItemViewHolder(itemView)
//    }
//
//    override fun onBindViewHolder(holder: CarItemViewHolder, position: Int) {
//        val currentItem = carList[position]
//        holder.carImage.setImageResource(currentItem.carImage)
//        holder.statusImage.setImageResource(currentItem.statusImage)
//        holder.textView1.text = currentItem.text1
//        holder.textView2.text = currentItem.text2
//        holder.textView3.text = currentItem.text3
//        holder.textView4.text = currentItem.text4
//        holder.licensePlate.text = currentItem.licensePlate
//
//    }
//
//    override fun getItemCount() = carList.size
//
//    inner class CarItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
//            View.OnClickListener {
//        val carImage: ImageView = itemView.findViewById(R.id.carImage)
//        val statusImage: ImageView = itemView.findViewById(R.id.statusImage)
//        val licensePlate: TextView = itemView.findViewById(R.id.license_plate)
//        var textView1 : TextView = itemView.findViewById(R.id.text_view1)
//        val textView2 : TextView = itemView.findViewById(R.id.text_view2)
//        val textView3 : TextView = itemView.findViewById(R.id.text_view3)
//        val textView4 : TextView = itemView.findViewById(R.id.text_view4)
//
//        init{
//            itemView.setOnClickListener(this)
//        }
//
//        override fun onClick(v: View?) {
//            val position = adapterPosition
//            if (position != RecyclerView.NO_POSITION){
//                listener.onItemClick(position)
//            }
//        }
//    }
//    interface OnItemClickListener {
//        fun onItemClick(position: Int)
//    }
//}
