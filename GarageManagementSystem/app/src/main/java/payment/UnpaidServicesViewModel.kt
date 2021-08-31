package payment

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dataclasses.Service
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class UnpaidServicesViewModel(private val userID: String): ViewModel(){
    private val db = Firebase.firestore
    private val userServiceListRef = db.collection("users").document(userID).collection("services")
    // A list of unpaid services so we can display them in the cart so the client cant pay for the unpaid services
    private val unpaidServiceList: MutableLiveData<ArrayList<Service>> by lazy{
        MutableLiveData<ArrayList<Service>>().also{
            loadUnPaidServicesFromFirebase()
        }
    }
    init{
        Log.d("Service List ViewModel","Create ViewModel")
        loadUnPaidServicesFromFirebase()
        // Code below deal with data changes to the database ref
        userServiceListRef
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(ContentValues.TAG, "listen:error", e)
                    return@addSnapshotListener
                }

                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> Log.d(ContentValues.TAG, "New city: ${dc.document.data}")
                        DocumentChange.Type.MODIFIED -> Log.d(ContentValues.TAG, "Modified city: ${dc.document.data}")
                        DocumentChange.Type.REMOVED -> Log.d(ContentValues.TAG, "Removed city: ${dc.document.data}")
                    }
                }
            }
    }
    // Boolean variable indicating the loading has finished: Other class can show progess bar, etc..
    val loadServicesDone = MutableLiveData<Boolean>()
    // Service we need to get the specific detail to display on the screen when the
    // user press the threedot button on the screen
    val checkServiceDetailEvent = MutableLiveData<Service>()

    fun getUnpaidServices(): LiveData<ArrayList<Service>> {
        return unpaidServiceList
    }
    // Fetching full list of services
    fun addNewUnpaidServerToFirebase(userPickedService : Service){
        userServiceListRef.add(userPickedService)
            .addOnSuccessListener {
                Log.d("ADD NEW SERVICE:", "New service is susccessfuly added to firestore")
            }
            .addOnFailureListener{
                Log.d("ADD NEW SERVICE:","Something went wrong. Try again later")
            }
    }

    fun removeService(userPickedService : Service){

    }
    // Fetching unpaid services only
    private fun loadUnPaidServicesFromFirebase(){
        // Do an asynchronous operation to fecth users
        val list = ArrayList<Service>()
        userServiceListRef
            .whereEqualTo("paid",false)
            .get()
            .addOnSuccessListener {result->
                for(document in result){
                    val service = document.toObject<Service>()
                    list.add(service)
                }
                unpaidServiceList.value = list
            }
            .addOnFailureListener{
                Log.d(ContentValues.TAG,"FAILED TO LOADED UNPAID SERVICES")
            }
    }
}