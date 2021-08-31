package customer.homepage.service

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.dataclasses.Car
import com.example.dataclasses.R
import com.example.dataclasses.ServiceItem


class ServiceItemAdapter(context: Context, objects: MutableList<ServiceItem>) :
    ArrayAdapter<ServiceItem>(context,0, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }
    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val currentItem: ServiceItem? = getItem(position)
        if (convertView  == null){
            val newConvertView = LayoutInflater.from(context).inflate(R.layout.service_item_spinner_row,parent,false)
            val serviceNameView = newConvertView.findViewById<TextView>(R.id.service_item_name)
            val serviceIconView = newConvertView.findViewById<ImageView>(R.id.service_item_icon)
            if (currentItem != null) {
                serviceIconView?.setImageResource(getServiceTypeDrawable(currentItem.type))
                if (serviceNameView != null) {
                    serviceNameView.text = currentItem.name
                }
            }
            return newConvertView
        }
        else{
            val serviceNameView = convertView.findViewById<TextView>(R.id.service_item_name)
            val serviceIconView = convertView.findViewById<ImageView>(R.id.service_item_icon)
            if (currentItem != null) {
                serviceIconView?.setImageResource(getServiceTypeDrawable(currentItem.type))
                if (serviceNameView != null) {
                    serviceNameView.text = currentItem.name
                }
            }
            return convertView
        }
    }



    private fun getServiceTypeDrawable(type : String)  : Int{
        return if (type == "repairing"){
            R.drawable.ic_baseline_settings_24
        } else{
            R.drawable.ic_baseline_local_car_wash_24
        }
    }
}

