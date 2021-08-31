package com.example.centergaragemanagementsystem.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.centergaragemanagementsystem.R
import com.example.centergaragemanagementsystem.adapter.CustomerItemAdapter
import com.example.centergaragemanagementsystem.dataClass.Customer
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CustomerListActivity : AppCompatActivity (){

    private val db = Firebase.firestore
    private lateinit var  adapter : CustomerItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView((R.layout.activity_customer_list))

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = "Danh sách khách hàng"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        Toast.makeText(this, "Outter Loading RecyclerView", Toast.LENGTH_SHORT).show()

        setUpRecyclerView()
        Toast.makeText(this, " Below Loading RecyclerView", Toast.LENGTH_SHORT).show()


    }


    override fun onStart(){
        super.onStart()
        adapter.startListening()
    }

    override fun onStop(){
        super.onStop()
        adapter.stopListening()
    }

    private fun setUpRecyclerView(){
        Log.d("CustomerItemViewHolder: ", "onCreatViewHoleder")
        Toast.makeText(this, "Inside Loading RecyclerView", Toast.LENGTH_SHORT).show()
        val customerList : Query = db.collection("Customer")

        val options = FirestoreRecyclerOptions.Builder<Customer>()
            .setQuery(customerList, Customer::class.java)
            .build()

        val layoutManager = LinearLayoutManager(this)
        adapter = CustomerItemAdapter(options)


        val recyclerView = findViewById<RecyclerView>(R.id.customerlistRecycleView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

    }


    private fun loadCarListPage(){
        val intent = Intent(this, CarListActivity::class.java)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
