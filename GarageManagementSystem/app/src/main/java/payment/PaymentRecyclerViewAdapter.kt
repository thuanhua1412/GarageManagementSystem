package payment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dataclasses.R
import com.example.dataclasses.Service

class PaymentRecyclerViewAdapter(private val serviceList: List<Service>,private val listener:ServiceItemClickListener): RecyclerView.Adapter<PaymentRecyclerViewAdapter.ServiceItemViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceItemViewHolder {
        val serviceItemObject = LayoutInflater.from(parent.context).inflate(R.layout.bill_recyclerview_item,parent,false)
        return ServiceItemViewHolder(serviceItemObject)
    }
    override fun onBindViewHolder(holder: ServiceItemViewHolder, position: Int) {
        val currentItem = serviceList[position]
        holder.serviceName.text = currentItem.name
        holder.cost.text = currentItem.cost.toString()
        holder.description.text = currentItem.description
    }
    override fun getItemCount(): Int {
        return serviceList.size
    }
    inner class ServiceItemViewHolder(ServiceItemView: View):RecyclerView.ViewHolder(ServiceItemView),View.OnClickListener{
        val serviceName: TextView = ServiceItemView.findViewById(R.id.service_name)
        val description: TextView = ServiceItemView.findViewById(R.id.service_description)
        val cost  = ServiceItemView.findViewById<TextView>(R.id.cost_text_view)
        init {
            ServiceItemView.setOnClickListener(this)
        }
        override fun onClick(p0: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                listener.onServiceItemClicked(position)
            }
        }

    }
    interface ServiceItemClickListener{
        fun onServiceItemClicked(position: Int)
    }

}