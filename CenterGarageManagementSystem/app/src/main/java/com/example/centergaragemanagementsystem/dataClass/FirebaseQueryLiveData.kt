package com.example.centergaragemanagementsystem.dataClass

import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.*

class FirebaseQueryLiveData(private val query: Query) : LiveData<QuerySnapshot>() {
    private val listener = DataChangeListener()
    private var listenerRegistration : ListenerRegistration? = null

    private var listenerRemovePending = false
    private val handler = Handler()

    private val removeListener = Runnable {
        listenerRegistration!!.remove()
        listenerRemovePending = false
    }

    override fun onActive() {
        super.onActive()
        Log.d("On Active:", "Firebase Listener of query: ${query.toString()}")
        if (listenerRemovePending){
            handler.removeCallbacks(removeListener)
        }
        else{
            listenerRegistration = query.addSnapshotListener(listener)
        }
        listenerRemovePending = false
    }

    override fun onInactive() {
        super.onInactive()
        Log.d("On InActive:", "Firebase Listener of query: ${query.toString()}")
        handler.postDelayed(removeListener, 2000)
        listenerRemovePending = true
    }

    private inner class DataChangeListener : EventListener<QuerySnapshot>{
        override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
            if(error != null){
                Log.e(
                    "Firebase Listener Error",
                    "Can't listen to query snapshot ${query.toString()}"
                )
                return
            }
            setValue(value!!)
        }
//        override fun onEvent(snapshots: QuerySnapshot?, e: FirebaseFirestoreException?) {
//            if (e != null) {
//                setValue(Resource(e))
//                return
//            }
//            setValue(Resource(documentToList(snapshots)))
//        }

    }
}