package com.example.centergaragemanagementsystem.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.centergaragemanagementsystem.R
import com.example.centergaragemanagementsystem.viewmodel.ServiceViewModel
import com.example.centergaragemanagementsystem.dataClass.Customer
import com.example.centergaragemanagementsystem.databinding.ActivityNhanXeCustomerInfoBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

open class NhanXeCustomerInfoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNhanXeCustomerInfoBinding
    private lateinit var serviceViewModel: ServiceViewModel
    private lateinit var customerID:String
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_nhan_xe_customer_info)

        Log.d("CUSTOMERINFO", "${intent.getStringExtra("newCustomer")}")
        if(intent.getStringExtra("newCustomer").toBoolean()){
            binding.continueButton.setOnClickListener {
                addCustomerToFireStore()
            }
        }
        else {
            displayCustomerInfo()
            binding.continueButton.setOnClickListener {
                loadCarInforPage()
            }
        }


        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = "Thông tin khách hàng"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


    }

    private fun addCustomerToFireStore() {
        val customer : Customer

        val name = binding.etName.text.toString()
        val mail = binding.etMail.text.toString()
        val phone = binding.etPhone.text.toString()
        if (validateInput(name, mail, phone)){
            customer = Customer(name = name, email = mail, phone = phone)
            db.collection("Customer")
                .add(customer)
                .addOnSuccessListener {
                    customerID = it.id
                    Log.d("Nhan Xe New Customer:","New customer added to firestore")
                    Toast.makeText(this, "customer added successful",
                        Toast.LENGTH_SHORT).show()
                    loadCarInforPage()
                }
                .addOnFailureListener {
                    Log.d("Nhan Xe New Customer:", "FAILED TO ADD CUSTOMER TO DATABASE")
                    Toast.makeText(this, "Failed to add customer to FireStore", Toast.LENGTH_SHORT).show()
                }
        }

    }

    private fun validateInput(name: String, mail: String, phone:String): Boolean {
        if (name == "" || mail == "" || phone == "") {
            Toast.makeText(this, "All field can't null", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun displayCustomerInfo() {
        val ownerID = intent.getStringExtra("ownerID").toString()
        val serviceID = intent.getStringExtra("serviceID").toString()
        Log.d("OWNERID", "$ownerID ")
        Log.d("SERVICEID", "$serviceID ")
        serviceViewModel = ViewModelProvider(this).get(ServiceViewModel()::class.java)
        serviceViewModel.getMergedLiveData().observe(this, Observer { triple ->
            val filteredCustomer = triple.third.find {
                it.email == ownerID
            }
            val filteredService = triple.first.find {
                it.serviceID == serviceID
            }
            binding.customer = filteredCustomer
            binding.service = filteredService
        })
        binding.etName.isEnabled = false
        binding.etMail.isEnabled = false
        binding.etPhone.isEnabled = false
    }

    private fun loadCarInforPage() {
        if (intent.getStringExtra("newCustomer").toBoolean()) {
            val intent = Intent(this, ArisingServiceActivity::class.java)
            intent.putExtra("customerID", customerID)
            intent.putExtra("newCustomer", "true")
            startActivity(intent)
        }
        else {
            val carServiced = intent.getStringExtra("carServiced").toString()
            val serviceID = intent.getStringExtra("serviceID").toString()
            val cartID = intent.getStringExtra("cartID").toString()
            val intent = Intent(this, NhanXeCarInfoActivity::class.java)
            intent.putExtra("carServiced", carServiced)
            intent.putExtra("serviceID", serviceID)
            intent.putExtra("cartID", cartID)
            intent.putExtra("newCustomer", "false")
            Log.d("NhanXeCustomerInfoServiceID: ", serviceID)
            startActivity(intent)
        }
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