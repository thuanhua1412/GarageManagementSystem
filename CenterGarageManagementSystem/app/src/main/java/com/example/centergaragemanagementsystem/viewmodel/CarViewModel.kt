package com.example.centergaragemanagementsystem.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.centergaragemanagementsystem.data.Repo
import com.example.centergaragemanagementsystem.dataClass.Car


class CarViewModel : ViewModel() {
    /*private var customers:MutableLiveData<List<Customer>> = MutableLiveData<List<Customer>>()*/
    /*private var customers:MutableLiveData<List<Customer>> = MutableLiveData<List<Customer>>() = MutableLiveData<List<Customer>>()*/
    private val  repo = Repo()
    fun fetchCarData(): LiveData<MutableList<Car>> {
        val mutableData = MutableLiveData<MutableList<Car>>()
        repo.getCarData().observeForever { carList ->
            mutableData.value = carList
        }
        return mutableData
    }
}
