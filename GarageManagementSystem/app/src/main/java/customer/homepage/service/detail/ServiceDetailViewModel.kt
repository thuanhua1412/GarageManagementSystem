package customer.homepage.service.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.dataclasses.FirebaseQueryLiveData
import com.example.dataclasses.Service
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ServiceDetailViewModel(service: Service) : ViewModel() {
    private val currentUser = FirebaseAuth.getInstance().currentUser

    private val serviceQuery  = Firebase.firestore.collection("Service")
        .whereEqualTo(FieldPath.documentId(), service.serviceID).limit(1)

    private val serviceLiveDataGetter = FirebaseQueryLiveData(serviceQuery)

    private val db = Firebase.firestore

    private var serviceLiveData : LiveData<Service> = Transformations.map(
        serviceLiveDataGetter
    ){
        Log.d("SVIEWMODEL:","Getting the service with ID: ${service.serviceID}")
        if(it.isEmpty){
            Log.d("ServiceViewModel:", "No Service Found")
        }
        val serVice = it.documents[0].toObject(Service::class.java)
        Log.d("SERVICELIST:", "$service")
        serVice
    }

    fun getServiceListLiveData() : LiveData<Service> {
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