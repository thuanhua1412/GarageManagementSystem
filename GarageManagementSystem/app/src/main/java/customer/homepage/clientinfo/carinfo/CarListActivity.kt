package customer.homepage.clientinfo.carinfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dataclasses.Car
import com.example.dataclasses.R
import com.example.dataclasses.databinding.ActivityCarListBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import customer.homepage.clientinfo.carinfo.cardetail.CarInfoActivity

class CarListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCarListBinding

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val db = Firebase.firestore

    private lateinit var adapter : CarListRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_car_list)


        setUpRecyclerView()

        binding.addCarButtonCarlist.setOnClickListener {
            val intent = Intent(this, AddCarActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    private fun setUpRecyclerView(){
        Log.d("Get Car List Of User: ", currentUser!!.email!!)
        val carListQuery : Query = db.collection("Car")
            .whereEqualTo("ownerID",currentUser.email)

        val options = FirestoreRecyclerOptions.Builder<Car>()
            .setQuery(carListQuery, Car::class.java)
            .build()
        // TODO: Connect Firebase Adapter to Recycler View
        adapter = CarListRecyclerViewAdapter(options, CarClickListener { car->
            navigateToCarDetailActivity(car)
        })
         val layoutManager = LinearLayoutManager(this)

        binding.carRecyclerView.adapter = adapter
        binding.carRecyclerView.layoutManager = layoutManager
    }

    private fun navigateToCarDetailActivity(car : Car){
        val intent = Intent(this, CarInfoActivity::class.java)
        // Pass the car class to the detail class for correct display
        intent.putExtra("car",car)
        startActivity(intent)
    }

}