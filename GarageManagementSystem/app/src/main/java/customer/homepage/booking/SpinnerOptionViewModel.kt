package customer.homepage.booking

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.dataclasses.Car
import com.example.dataclasses.FirebaseQueryLiveData
import com.example.dataclasses.Service
import com.example.dataclasses.ServiceItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class SpinnerOptionViewModel  : ViewModel(){
    //private var carList  = ArrayList<Car>()
    private val currentUser = FirebaseAuth.getInstance().currentUser!!
    private val carQuery = Firebase.firestore.collection("Car").whereEqualTo("ownerID",currentUser.email)
    private val availableServiceQuery = Firebase.firestore.collection("AvailableService")

    private val liveData  = FirebaseQueryLiveData(carQuery)
    private val liveData2 = FirebaseQueryLiveData(availableServiceQuery)

    private var carListLiveData : LiveData<ArrayList<Car>> = Transformations.map(liveData){
        val carList =  ArrayList<Car>()
        if(it.isEmpty){
            Log.d("Spinner Car:","No car added")
        }
        for(dc in it.documents){
            val car = dc.toObject(Car::class.java)
            if (car != null) {
                carList.add(car)
            }
        }
        Log.w("CARLIST:","$carList")
        carList
    }
    fun getCarListLiveData(): LiveData<ArrayList<Car>> {
        return carListLiveData
    }


    private var availableServiceLiveData : LiveData<ArrayList<ServiceItem>> = Transformations.map(liveData2){
        val availableServiceList = ArrayList<ServiceItem>()
        if(it.isEmpty){
            Log.d("Spinner Car:","No car added")
        }
        for(dc in it.documents){
            val serviceOptionItem = dc.toObject(ServiceItem::class.java)
            if (serviceOptionItem != null) {
                availableServiceList.add(serviceOptionItem)
            }
        }
        Log.w("CARLIST:","$availableServiceList")
        availableServiceList
    }

    fun getAvailableServiceLiveData() : LiveData<ArrayList<ServiceItem>>{
        return availableServiceLiveData
    }



    fun getCarList() : ArrayList<Car> {
        return if(carListLiveData.value == null){
            Log.w("CARLIST:","NULLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL")

            val fakeCarList = ArrayList<Car>()
            fakeCarList.add(Car("FakeCar","Ferrari","fakeUser","null"))
            fakeCarList
        } else{
            Log.w("CARLIST:","CARLIST NOT NULLL: ${carListLiveData.value}")
            carListLiveData.value!!
        }
    }
}