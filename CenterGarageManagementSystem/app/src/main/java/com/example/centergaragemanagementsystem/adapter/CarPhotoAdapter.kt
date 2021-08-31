package com.example.centergaragemanagementsystem.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.centergaragemanagementsystem.dataClass.Service
import com.example.centergaragemanagementsystem.databinding.CarImageItemBinding
import com.squareup.picasso.Picasso

//class CarPhotoClickListener(val clickListener :(service: Service ) -> Unit){
//    fun onClick(service: Service) = clickListener(service)
//}

class CarPhotoAdapter(private val service: Service?): RecyclerView.Adapter<CarPhotoAdapter.CarPhotoItemViewHolder>() {
    class CarPhotoItemViewHolder private constructor(val binding: CarImageItemBinding, val service: Service?):RecyclerView.ViewHolder(binding.root) {
        fun bind(imageURI:String, position: Int) {
            Log.d("CARPHOTOVIEWHOLDERBIND: ", imageURI)
            Picasso.get().load(imageURI).into(binding.carPhoto)
            val pos = position+1
            val text = "$pos/${service!!.carImage.size}"
            binding.tvNumOfPic.text = text
        }
        companion object{
            fun from(parent: ViewGroup, service:Service?) : CarPhotoItemViewHolder {
                val layoutInflater  = LayoutInflater.from(parent.context)
                val binding = CarImageItemBinding.inflate(layoutInflater,parent,false)

                return CarPhotoItemViewHolder(binding, service)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarPhotoItemViewHolder {
        Log.d("Car Photo Item Adapter", "Creating Car Photo Item ViewHolder")
        return CarPhotoItemViewHolder.from(parent, service)
    }

    override fun onBindViewHolder(holder: CarPhotoItemViewHolder, position: Int) {
        holder.bind(service!!.carImage[position], position)
    }

    override fun getItemCount(): Int {
        return service!!.carImage.size
    }
}