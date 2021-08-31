package com.example.centergaragemanagementsystem.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.centergaragemanagementsystem.dataClass.Car
import com.example.centergaragemanagementsystem.dataClass.Customer
import com.google.firebase.firestore.FirebaseFirestore

class Repo {
    fun getCustomerData():LiveData<MutableList<Customer>>{
        val mutableData =  MutableLiveData<MutableList<Customer>>()
        FirebaseFirestore.getInstance().collection("Customer").get()
            .addOnSuccessListener {result ->
                val listData = mutableListOf<Customer>()
                for (document in result){
                    val customer = document.toObject(Customer::class.java)
                    listData.add(customer)
                }
                //Error.
                mutableData.value = listData
            }

        return mutableData
    }

    fun getCarData():LiveData<MutableList<Car>>{
        val mutableData =  MutableLiveData<MutableList<Car>>()
        FirebaseFirestore.getInstance().collection("Car").get()
            .addOnSuccessListener {result ->
                val listData = mutableListOf<Car>()
                for (document in result){
                    val car = document.toObject(Car::class.java)
                    listData.add(car)
                }
                //Error.
                mutableData.value = listData
            }

        return mutableData
    }
}