package customer.homepage.clientinfo.carinfo.cardetail.placeholder

import androidx.lifecycle.ViewModel


import android.media.audiofx.NoiseSuppressor.create
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.example.dataclasses.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class CarSpecificServiceViewModel : ViewModel() {
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val carQuery = Firebase.firestore.collection("Car").whereEqualTo(
        "ownerID",
        currentUser.email
    )
    private val serviceQuery  = Firebase.firestore.collection("Service")
        .whereEqualTo("ownerID", currentUser.email)

    private val serviceLiveDataGetter = FirebaseQueryLiveData(serviceQuery)

    private val db = Firebase.firestore

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

    fun getServiceListLiveData() : LiveData<ArrayList<Service>>{
        return serviceLiveData
    }

    fun removeService(service : Service){
        db.collection("Service").document(service.serviceID)
            .delete()
            .addOnSuccessListener {
                Log.d("SERVICE_REMOVE","SUCCESSFULY REMOVE A SERVICE")
            }
            .addOnFailureListener {
                Log.d("SERVICE_REMOVAL","Failed to remove service.")
            }
    }

    fun paidService(service: Service){
        db.collection("Service").document(service.serviceID)
            .update("paid",true)
            .addOnSuccessListener {
                Log.d("SERVICE_REMOVE","SUCCESSFULY PAID A SERVICE")
            }
            .addOnFailureListener {
                Log.d("SERVICE_REMOVAL","Failed to remove service: $it.")
            }
    }

}

