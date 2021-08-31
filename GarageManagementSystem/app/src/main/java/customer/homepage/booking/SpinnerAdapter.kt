package customer.homepage.booking

import android.content.Context
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.dataclasses.Car
import com.example.dataclasses.R
import com.example.dataclasses.ServiceItem
import com.example.dataclasses.databinding.CarItemSpinnerRowBinding
import com.example.dataclasses.databinding.ServiceItemSpinnerRowBinding


//class MultiSelectionSpinner : androidx.appcompat.widget.AppCompatSpinner()


class CarSpinnerAdapter(context: Context, objects: MutableList<Car>) :
    ArrayAdapter<Car>(context,0, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }
    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val currentItem : Car? = getItem(position)

        val carItemInflater = LayoutInflater.from(parent.context)
        val binding = CarItemSpinnerRowBinding.inflate(carItemInflater,parent,false)
        binding.carItem = currentItem
        return binding.root
    }
}

class AvailableServiceSpinnerAdapter(context: Context, objects: MutableList<ServiceItem>) :
    ArrayAdapter<ServiceItem>(context,0, objects) {

    private val selectedServices  = ArrayList<ServiceItem>()
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }
    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val currentItem : ServiceItem? = getItem(position)

        val serviceItemInflater = LayoutInflater.from(parent.context)
        val binding = ServiceItemSpinnerRowBinding.inflate(serviceItemInflater,parent,false)
        binding.checkbox.isChecked = selectedServices.contains(currentItem)
        binding.serviceItem = currentItem
        binding.addButton.setOnClickListener {
            Toast.makeText(this.context, "add service button clicked", Toast.LENGTH_SHORT).show()
            if (currentItem != null) {
                if(!selectedServices.contains(currentItem)){
                    selectedServices.add(currentItem)
                    binding.checkbox.isChecked= true
                }
//                Toast.makeText(this.context, "service addedw", Toast.LENGTH_SHORT).show()
            }
            Log.d("SELECTED_SERVICES","$selectedServices")
        }
        binding.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                if (currentItem != null) {
                    if(!selectedServices.contains(currentItem)){
                        selectedServices.add(currentItem)
                        binding.checkbox.isChecked= true
                    }
//                    Toast.makeText(this.context, "service addedw", Toast.LENGTH_SHORT).show()
                    Log.d("ADD_CHECK_SERVICES","$selectedServices")
                }
            }
            else{
                if(selectedServices.contains(currentItem)){
                    selectedServices.remove(currentItem)
                    binding.checkbox.isChecked = false
                }
//                Toast.makeText(this.context, "service removed", Toast.LENGTH_SHORT).show()
                Log.d("REMOVE_CHECK_SERVICES","$selectedServices")

            }
        }
        binding.removeButton.setOnClickListener {
            Toast.makeText(this.context, "remove service button clicked", Toast.LENGTH_SHORT).show()
            if (currentItem != null) {
                if(selectedServices.contains(currentItem)){
                    selectedServices.remove(currentItem)
                    binding.checkbox.isChecked = false
                }
//                Toast.makeText(this.context, "service removed", Toast.LENGTH_SHORT).show()
            }
            Log.d("SELECTED_SERVICES","$selectedServices")
        }
        return binding.root
    }

    private fun checkAndRemoveService(service: ServiceItem) {
        if(selectedServices.contains(service)){
            selectedServices.remove(service)
        }
    }

    private fun checkAndAddService(service : ServiceItem){
        if(!selectedServices.contains(service)){
            selectedServices.add(service)

        }
    }
    fun getSelectedServices(): ArrayList<ServiceItem> {
        return selectedServices
    }
//    class listener : CompoundButton.OnCheckedChangeListener{
//        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
//            if (isChecked){
//
//            }
//            else{
//
//            }
//        }
//
//    }
}



