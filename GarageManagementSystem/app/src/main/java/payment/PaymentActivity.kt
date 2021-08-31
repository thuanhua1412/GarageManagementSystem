package payment

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dataclasses.Observer
import com.example.dataclasses.R
import com.example.dataclasses.Service
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.sql.Timestamp

class PaymentActivity : AppCompatActivity(),PaymentRecyclerViewAdapter.ServiceItemClickListener,Observer {
    private lateinit var serviceList: ArrayList<Service>
    private lateinit var adapter : PaymentRecyclerViewAdapter
    private val db = Firebase.firestore
    private val userID = "[FIX THIS ONE]"
//    private val Subject  =
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        setSupportActionBar(findViewById(R.id.toolbar))
    // TODO: Implement logging and authentication to pass the auto-generated UserID to the home screen activity
    val userID : String = "me"
    val viewModel: UnpaidServicesViewModel by viewModels { MyViewModelFactory(userID) }

    val recyclerView = findViewById<RecyclerView>(R.id.serviceRecyclerView)
        val layoutManager = LinearLayoutManager(this)
//        adapter = PaymentRecyclerViewAdapter(viewModel.getUnpaidServices(),this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

//        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
    }
//    private fun setupRecyclerView(){
//        val query: Query  =  db.collection("users")
//            .document(userID)
//            .collection("services")
//
//        val options = FirestoreRecyclerOptions.Builder<Service>()
//            .setQuery(query, Service)
//
//    }

    private fun update(size:Int) : List<Service>{
        val list = ArrayList<Service>()
        val smockService = Service("name",pickUpLocation = "anywhere"
            , description = "funny",paid =false,cost = 1000000)
        for(i in 0 until size){
            val id = i
            val name = "Do service with id $i"
            val pickupLocation = "at location $i"
            val timestamp = Timestamp(0L)
            val description = "Number $i is funny as hell"
            val cost = i * 100000
            val item = Service(name = name, pickUpLocation = pickupLocation,description = description,cost = cost)
            list+= item
        }
        return list
    }


    override fun onServiceItemClicked(position: Int) {
        Toast.makeText(this, "Item $position clicked", Toast.LENGTH_SHORT).show()
        val clickedItem = serviceList[position]
        clickedItem.description = "Clicked"
        adapter?.notifyItemChanged(position)
    }

    override fun update(data: Any) {
        TODO("Not yet implemented")

    }
}