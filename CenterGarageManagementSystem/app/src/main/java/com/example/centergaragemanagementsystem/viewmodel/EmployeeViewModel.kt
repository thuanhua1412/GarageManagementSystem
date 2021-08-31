package com.example.centergaragemanagementsystem.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.centergaragemanagementsystem.dataClass.FirebaseQueryLiveData
import com.example.centergaragemanagementsystem.dataClass.Manager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class EmployeeViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val userQuery = db.collection("User").whereEqualTo("email", currentUser?.email)
    private val userLiveDataGetter = FirebaseQueryLiveData(userQuery)

    private var userLiveData : LiveData<Manager> = Transformations.map(userLiveDataGetter) {
        if (it.isEmpty) {
            Log.d("EmployeeViewModel:", "No user Found")
        }
        var user = Manager()

        for (dc in it.documents) {
            user = dc.toObject(Manager::class.java)!!
            user.userID = dc.id
        }

        user
    }
    fun getUserLiveData() : LiveData<Manager> {
        return userLiveData
    }

    fun UpdateUserImage(user: Manager, ImageURI:String) {
        db.collection("User").document(user.userID).update("avatarURI", ImageURI)
            .addOnSuccessListener {
                Log.d("UPDATEIMAGEURI: ", "update successful")
            }
            .addOnFailureListener {
                Log.d("UPDATIMAGEURI: ", "update fail, try agian")
            }
    }
}