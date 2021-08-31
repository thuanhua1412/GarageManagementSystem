package customer.homepage.service

import android.media.audiofx.NoiseSuppressor.create
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.example.dataclasses.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ServiceViewModel(fromClient:String) : ViewModel() {
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val carQuery = Firebase.firestore.collection("Car").whereEqualTo(
        "ownerID",
        currentUser.email
    )
    private val clientSource : Boolean = fromClient == BOOKED_BY_CLIENT

    private val serviceQuery  = Firebase.firestore.collection("Service")
        .whereEqualTo("ownerID", currentUser.email).whereEqualTo("fromClient",clientSource)

    private val carLiveDataGetter = FirebaseQueryLiveData(carQuery)
    private val serviceLiveDataGetter = FirebaseQueryLiveData(serviceQuery)

    private val db = Firebase.firestore
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
        Log.d("SERVICELIST:", "$serviceList  $clientSource")
        Log.d("SERVICELIST_HERE:", "clientSource: $clientSource")
        serviceList
    }

    fun getCarListLiveData() : LiveData<ArrayList<Car>>{
        return carLiveData
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

    fun paidService(serviceID: String){
        db.collection("Service").document(serviceID)
            .update("paid",true)
            .addOnSuccessListener {
                Log.d("SERVICE_REMOVE","SUCCESSFULY PAID A SERVICE")
            }
            .addOnFailureListener {
                Log.d("SERVICE_REMOVAL","Failed to remove service: $it.")
            }
    }

    class CombinedLiveData(ld1: LiveData<ArrayList<Service>>, ld2: LiveData<ArrayList<Car>>) :
        MediatorLiveData<Pair<ArrayList<Service>, ArrayList<Car>>>() {
        private var services: ArrayList<Service> = ArrayList()
        private var cars: ArrayList<Car> = ArrayList()

        init {
            value = Pair(services,cars)
            addSource<ArrayList<Service>>(ld1
            ) { services: ArrayList<Service> ->
                this.services = services
                value = Pair(services, cars)
            }
            addSource<ArrayList<Car>>(ld2
            ) { cars: ArrayList<Car> ->
                this.cars = cars
                value = Pair(services, cars)
            }
        }
    }

    private val liveDataMerger = CombinedLiveData(serviceLiveData,carLiveData)

    fun getMergedLiveData() : CombinedLiveData{
        return liveDataMerger
    }
}

