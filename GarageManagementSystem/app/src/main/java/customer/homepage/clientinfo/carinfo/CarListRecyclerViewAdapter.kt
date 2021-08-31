package customer.homepage.clientinfo.carinfo

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dataclasses.Car
import com.example.dataclasses.Service
import com.example.dataclasses.databinding.CarListItemBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class CarClickListener(val clickListener : (Car) -> Unit){
    fun onClick(car: Car) = clickListener(car)
}
class CarListRecyclerViewAdapter(options : FirestoreRecyclerOptions<Car>, private val carClickListener: CarClickListener)  :FirestoreRecyclerAdapter<Car, CarListRecyclerViewAdapter.CarItemViewHolder>(options){
    class CarItemViewHolder private constructor(val binding: CarListItemBinding)
        : RecyclerView.ViewHolder(binding.root){

        fun bind(item : Car, listener: CarClickListener){
            binding.carItem = item
            binding.clickListener = listener
        }

        companion object{
            // Inflate the view of the Car Item to the screen so we can bind data to them
            fun from(parent: ViewGroup) : CarItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding  = CarListItemBinding.inflate(layoutInflater,parent,false)

                return CarItemViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarItemViewHolder {
        Log.d("Car Item Adpater:", "Creating Car Item ViewHolder")
        return CarItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CarItemViewHolder, position: Int, model: Car) {
        holder.bind(model, carClickListener)
    }
}