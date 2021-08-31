package com.example.centergaragemanagementsystem.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.centergaragemanagementsystem.data.Repo
import com.example.centergaragemanagementsystem.dataClass.Customer

class   CustomerViewModel : ViewModel() {
    /*private var customers:MutableLiveData<List<Customer>> = MutableLiveData<List<Customer>>()*/
    /*private var customers:MutableLiveData<List<Customer>> = MutableLiveData<List<Customer>>() = MutableLiveData<List<Customer>>()*/
    private val  repo = Repo()
    fun fetchCustomerData(): LiveData<MutableList<Customer>>{
        val mutableData = MutableLiveData<MutableList<Customer>>()
        repo.getCustomerData().observeForever { customerList ->
            mutableData.value = customerList
        }
        return mutableData
    }
}