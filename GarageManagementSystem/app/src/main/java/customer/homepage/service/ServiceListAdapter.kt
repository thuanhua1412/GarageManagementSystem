package customer.homepage.service

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dataclasses.*
import com.example.dataclasses.databinding.CarServiceItemBinding

//TODO: Currently, The finished service may not be paid yet. And the services
// can only be paid if they are at least approved.
class ServiceClickListener(val clickListener :(Pair<Service,String>) -> Unit){
    fun onClick(service: Service, type: String) = clickListener(Pair(service,type))
}

class ServiceListAdapter(private val serviceList: ArrayList<Service>, private val carList: ArrayList<Car>,private val clickListener:ServiceClickListener, private val viewModel: ServiceViewModel) : RecyclerView.Adapter<ServiceListAdapter.ServiceItemViewHolder>(){
    class ServiceItemViewHolder private constructor(val binding: CarServiceItemBinding,val carList: ArrayList<Car>, val viewModel: ServiceViewModel) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Service, clickListener: ServiceClickListener){
            binding.serviceItem = item
            binding.car = getCarFromService(item)
            binding.serviceStatus.setImageResource(getServiceStatusDrawableFromString(item.status))
            binding.clickListener = clickListener

            if(item.status == SERVICE_CONFIRMATION_WAITING){
                binding.paymentButton.visibility = View.GONE
                binding.reworkRequestButton.text = SERVICE_CANCEL_ACTION
                binding.reworkRequestButton.setOnClickListener {
                    Log.d("CANCEL_SERVICE_CLICKED","Adapter Cancel Service Click: $item")
                    viewModel.removeService(item)
                }
            }
            else if(item.status == SERVICE_FINISHED){
                binding.reworkRequestButton.text = SERVICE_REWORK_REQUEST_ACTION
                binding.reworkRequestButton.setOnClickListener {
                    Log.d("REWORK_SERVICE_CLICKED","Currently Donothing $item")
                }
            }
            else{
                binding.reworkRequestButton.visibility = View.GONE
            }

            //TODO: Already handled with the databinding expression in the layout (Check car_service_item.xml)
//            binding.paymentButton.setOnClickListener {
//                Log.d("PAY_SERVICE_CLICKED","Pay Clicked Service: $item")
//                viewModel.paidService(item)
//            }
//            binding.serviceDetailButton.setOnClickListener {
//                Log.d("DETAIL_SERVICE_CLICKED","Detail Clicked Service:$item")
//            }
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
            fun from(parent: ViewGroup, carList: ArrayList<Car>, viewModel: ServiceViewModel) : ServiceItemViewHolder{
                val layoutInflater  = LayoutInflater.from(parent.context)
                val binding = CarServiceItemBinding.inflate(layoutInflater,parent,false)

                return ServiceItemViewHolder(binding,carList,viewModel)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceItemViewHolder {
        Log.d("Service Item Adapter", "Creating Service Item ViewHolder")
        return ServiceItemViewHolder.from(parent,carList,viewModel)
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