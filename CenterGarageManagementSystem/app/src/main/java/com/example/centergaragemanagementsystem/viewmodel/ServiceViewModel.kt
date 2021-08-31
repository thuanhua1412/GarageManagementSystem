package com.example.centergaragemanagementsystem.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.centergaragemanagementsystem.dataClass.Car
import com.example.centergaragemanagementsystem.dataClass.Customer
import com.example.centergaragemanagementsystem.dataClass.FirebaseQueryLiveData
import com.example.centergaragemanagementsystem.dataClass.Service
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ServiceViewModel () : ViewModel() {
    //private val currentUser = FirebaseAuth.getInstance().currentUser
    //private var carQuery = Firebase.firestore.collection("Car").whereEqualTo( "ownerID", currentUser.email )
    //private val serviceQuery  = Firebase.firestore.collection("Service")
    private val db = Firebase.firestore
    //private var carQuery : Query
    private val serviceQuery = db.collection("Service")
    private val carQuery = db.collection("Car")
    private val customerQuery = db.collection("Customer")


    private val carLiveDataGetter = FirebaseQueryLiveData(carQuery)
    private val serviceLiveDataGetter = FirebaseQueryLiveData(serviceQuery)
    private val customerLiveDataGetter = FirebaseQueryLiveData(customerQuery)

    private var carLiveData : LiveData<ArrayList<Car>> = Transformations.map(carLiveDataGetter){
        val carList = ArrayList<Car>()
        if(it.isEmpty){
            Log.d("ServiceViewModel:", "No car Found")
        }
        for(dc in it.documents){
            val car = dc.toObject(Car::class.java)
            if(car!=null){
                carList.add(car)
            }
        }
        Log.d("CARLISTSERVICE:", "$carList")
        carList
    }

    private var serviceLiveData : LiveData<ArrayList<Service>> = Transformations.map(
            serviceLiveDataGetter
    ){
        val serviceList = ArrayList<Service>()
        if(it.isEmpty){
            Log.d("ServiceViewModel:", "No Service Found")
        }
        for(dc in it.documents){
            val service = dc.toObject(Service::class.java)
            if(service != null){
                service.serviceID = dc.id
                serviceList.add(service)
            }
        }
        Log.d("SERVICELIST:", "$serviceList")
        serviceList
    }

    private var customerLiveData : LiveData<ArrayList<Customer>> = Transformations.map(customerLiveDataGetter){
        val customerList = ArrayList<Customer>()
        if(it.isEmpty){
            Log.d("ServiceViewModel:", "No customer Found")
        }
        for(dc in it.documents){
            val cus = dc.toObject(Customer::class.java)
            if(cus != null){
                customerList.add(cus)
            }
        }
        Log.d("CUSTOMERLISTSERVICE:", "$customerList")
        customerList
    }


    fun getCarListLiveData() : LiveData<ArrayList<Car>> {
        return carLiveData
    }

    fun getServiceListLiveData() : LiveData<ArrayList<Service>> {
        return serviceLiveData
    }

    fun getCustomerListLiveData() : LiveData<ArrayList<Customer>> {
        return customerLiveData
    }

    class CombinedLiveData(ld1: LiveData<ArrayList<Service>>, ld2: LiveData<ArrayList<Car>>, ld3: LiveData<ArrayList<Customer>>) :
            MediatorLiveData<Triple<ArrayList<Service>, ArrayList<Car>, ArrayList<Customer>>>() {
        private var services: ArrayList<Service> = ArrayList()
        private var cars: ArrayList<Car> = ArrayList()
        private var customer: ArrayList<Customer> = ArrayList()

        init {
            value = Triple(services, cars, customer)
            addSource<ArrayList<Service>>(ld1
            ) { services: ArrayList<Service> ->
                this.services = services
                value = Triple(services, cars, customer)
            }
            addSource<ArrayList<Car>>(ld2
            ) { cars: ArrayList<Car> ->
                this.cars = cars
                value = Triple(services, cars, customer)
            }
            addSource<ArrayList<Customer>>(ld3
            ) { customer: ArrayList<Customer> ->
                this.customer = customer
                value = Triple(services, cars, customer)
            }
        }
    }

    private val liveDataMerger = CombinedLiveData(serviceLiveData, carLiveData, customerLiveData)

    fun getMergedLiveData() : CombinedLiveData {
        return liveDataMerger
    }

    fun updateServicePhoto(serviceID: String, imageURI: String) {
        db.collection("Service").document(serviceID).get().addOnSuccessListener {
            val service = it.toObject<Service>()
            service!!.carImage.add(imageURI)
            val photo = hashMapOf("carImage" to service.carImage)
            db.collection("Service").document(serviceID).set(photo, SetOptions.merge())
                    .addOnSuccessListener {
                        Log.d("UPDATESERVICEIMAGEURI: ", "update successful")
                    }
                    .addOnFailureListener {
                        Log.d("UPDATESERVICEIMAGEURI: ", "update fail, try agian")
                    }
        }
    }

    fun updateCarServicedInfo(serviceID: String, speedometer : String, vatDung:String) {
        val updateField = hashMapOf("speedometer" to speedometer,
                                    "vatDung" to vatDung)
        db.collection("Service").document(serviceID).set(updateField, SetOptions.merge())
                .addOnSuccessListener {
                    Log.d("UPDATECARSERVICEDINFO: ", "update successful")
                }
                .addOnFailureListener {
                    Log.d("UPDATECARSERVICEDINFO: ", "update fail, try agian")
                }
    }

    fun updateServiceStatus(serviceID: String, status: String) {
        val updateField = hashMapOf("status" to status)
        db.collection("Service").document(serviceID).set(updateField, SetOptions.merge())
                .addOnSuccessListener {
                    Log.d("UPDATESERVICESTATUS: ", "update successful")
                }
                .addOnFailureListener {
                    Log.d("UPDATESERVICESTATUS: ", "update fail, try agian")
                }
    }

}