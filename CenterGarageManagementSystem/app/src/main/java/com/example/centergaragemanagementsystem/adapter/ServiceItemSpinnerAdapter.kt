package com.example.centergaragemanagementsystem.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.centergaragemanagementsystem.dataClass.Service
import com.example.centergaragemanagementsystem.databinding.ServiceItemBinding

class ServiceItemSpinnerAdapter(context: Context, objects: ArrayList<Service>) :
        ArrayAdapter<Service>(context, 0, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val serviceItemInflater = LayoutInflater.from(parent.context)
        val binding = ServiceItemBinding.inflate(serviceItemInflater,parent,false)
        binding.service = getItem(position)
        binding.checkboxItem.visibility = View.GONE
        return binding.root
    }

}