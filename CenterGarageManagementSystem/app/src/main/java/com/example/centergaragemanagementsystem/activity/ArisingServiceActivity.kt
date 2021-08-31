package com.example.centergaragemanagementsystem.activity

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.centercentergaragemanagementsystem.dataClass.Bill
import com.example.centergaragemanagementsystem.R
import com.example.centergaragemanagementsystem.viewmodel.ServiceViewModel


import com.example.centergaragemanagementsystem.adapter.ArisingServiceAdapter
import com.example.centergaragemanagementsystem.adapter.ArisingServiceClickListener
import com.example.centergaragemanagementsystem.dataClass.Customer
import com.example.centergaragemanagementsystem.dataClass.SERVICE_EXECUTING
import com.example.centergaragemanagementsystem.dataClass.Service
import com.example.centergaragemanagementsystem.dataClass.ServiceItem
import com.example.centergaragemanagementsystem.databinding.ActivityArisingServiceBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ArisingServiceActivity : AppCompatActivity() {
    private lateinit var binding : ActivityArisingServiceBinding
    private val db = Firebase.firestore
    private lateinit var adapter: ArisingServiceAdapter
    private  var serviceItemList = ArrayList<ServiceItem>()
    private lateinit var serviceID:String
    private lateinit var cartID:String
    private lateinit var allCurrentService: ArrayList<Service>
    private lateinit var serviceViewModel : ServiceViewModel
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_arising_service)
        serviceViewModel = ViewModelProvider(this).get(ServiceViewModel()::class.java)
        val toolbar: Toolbar = findViewById(R.id.toolbar)



        if (intent.getStringExtra("newCustomer")!!.toBoolean()) {
            toolbar.title = "Chọn dịch vụ"
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)


            binding.btnAdd.setOnClickListener {
                btnAddClicked()
                updateTotalCost()
            }

            binding.btnComplete.setText("Tiếp tục")
            binding.btnComplete.setOnClickListener {
                addServiceToFireStore()
            }
        }
        else {
            toolbar.title = "Dịch vụ phát sinh"

            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)

            serviceID = intent.getStringExtra("serviceID").toString()
            cartID = intent.getStringExtra("cartID").toString()
            serviceViewModel.getServiceListLiveData().observe(this, Observer { serviceList ->
                allCurrentService = ArrayList(serviceList.filter {
                    it.cartID == cartID
                })
            })
            binding.btnAdd.setOnClickListener {
                btnAddClicked()
                updateTotalCost()
            }
            binding.btnComplete.setOnClickListener{
                addArisingService()
            }
        }



    }

    private fun addServiceToFireStore() {
        Log.d("ARISING_SERVICES:", "$serviceItemList")
        if(serviceItemList.isEmpty()){
            Toast.makeText(this, "Can't continue without any service.", Toast.LENGTH_SHORT).show()
        }
        else {
            // Create a bill containing all the services chosen
            val bill = Bill(timeCreated = System.currentTimeMillis().toString(), totalBill = binding.etTotalCost.text.toString().toInt())
            val customerID = intent.getStringExtra("customerID").toString()

            db.collection("Customer").document(customerID).get().addOnSuccessListener {
                val customer = it.toObject(Customer::class.java)
                db.collection("Bill").add(bill)
                    .addOnSuccessListener { DocRef ->
                        var serviceID = ""
                        for(serviceItem in serviceItemList) {
                            val service = Service(name = serviceItem.name,
                                cost = serviceItem.cost,
                                timeLimit = serviceItem.timeLimit,
                                cartID = DocRef.id, fromClient = false,
                                ownerID = customer!!.email,
                                status = SERVICE_EXECUTING, description = serviceItem.description)

                            db.collection("Service").add(service)
                                .addOnSuccessListener {
                                    serviceID = it.id
                                    Log.d("SERVICE_ADDED:","${service.name} by ${service.ownerID}")
                                }
                                .addOnFailureListener { exception ->
                                    Log.e("SERVICE_ADDED:",exception.toString())
                                }
                        }
                        Log.d("AAAAAAAAAAAAAA", "$serviceID adfgh")
                        loadNextPage(DocRef.id, serviceID)

                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to create Bill. $it", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun loadNextPage(cartID:String, serviceID:String) {
        val customerID = intent.getStringExtra("customerID")
        val intent = Intent(this, NhanXeCarInfoActivity::class.java)
        intent.putExtra("customerID", customerID)
        intent.putExtra("cartID", cartID)
        intent.putExtra("serviceID", serviceID)
        intent.putExtra("newCustomer", "true")
        startActivity(intent)

    }


    private fun updateTotalCost() {
        binding.etTotalCost.text = serviceItemList.sumBy {
            it.cost
        }.toString()
    }


    private fun addArisingService() {
        // ADD ARISING SERVICE FOR APP CUSTOMER
        Log.d("ARISING_SERVICES:", "$serviceItemList")
        if(serviceItemList.isEmpty()){
            showDialog()
        }
        else {
            // Create a bill containing all the services chosen
            val bill = Bill(timeCreated = System.currentTimeMillis().toString(), totalBill = binding.etTotalCost.text.toString().toInt())

            db.collection("Service").document(serviceID).get().addOnSuccessListener {
                val thisService = it.toObject(Service::class.java)
                db.collection("Bill").add(bill)
                    .addOnSuccessListener { DocRef ->
                        for(serviceItem in serviceItemList) {
                            val service = Service(name = serviceItem.name,
                                cost = serviceItem.cost,
                                timeLimit = serviceItem.timeLimit,
                                cartID = DocRef.id, fromClient = false,
                                ownerID = thisService!!.ownerID, carServiced = thisService.carServiced,
                                status = SERVICE_EXECUTING, description = serviceItem.description)

                            db.collection("Service").add(service)
                                .addOnSuccessListener {
                                    Log.d("SERVICE_ADDED:","${service.name} by ${service.ownerID}")
                                }
                                .addOnFailureListener { exception ->
                                    Log.e("SERVICE_ADDED:",exception.toString())
                                }
                        }
                        loadHomePage()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to create Bill. ${it.toString()}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle("Warning")
        builder.setMessage("Không có bất kỳ dịch vụ nào phát sinh. Tiếp tục?")

        builder.setNegativeButton("Cancle", DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.cancel()
        })

        builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
            loadHomePage()
        })
        builder.show()
    }

    private fun loadHomePage() {
        for(service in allCurrentService){
            serviceViewModel.updateServiceStatus(service.serviceID, SERVICE_EXECUTING)
        }
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // clean up all activity
        startActivity(intent)
        finish()
    }

    private fun btnAddClicked() {
        val name = binding.etServiceItemName.text.toString()
        val description = binding.etServiceItemDescription.text.toString()
        val timeLimit = binding.etServiceItemTimeLImit.text.toString()
        val cost = binding.etCost.text.toString()
        Log.d("ArisingServiceAddButton: ", "$name, $description, $timeLimit, $cost")
        val checkCost = cost.toIntOrNull()
        if (checkCost != null) {
            if (name != "" && description != "" && timeLimit != "" && cost != "") {
                val serviceItem = ServiceItem(name, description, cost.toInt(), timeLimit, "arising" )
                serviceItemList.add(serviceItem)
                adapter = ArisingServiceAdapter(serviceItemList, ArisingServiceClickListener{
                    adapter.removeAt(it)
                    updateTotalCost()
                })
                binding.recyclerView.adapter = adapter
                binding.recyclerView.layoutManager = LinearLayoutManager(this)
                clearEditText()
            }
            else {
                Toast.makeText(this, "All field cannot be null", Toast.LENGTH_SHORT).show()
            }
        }
        else {
            Toast.makeText(this, "Cost must be numberic", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearEditText() {
        binding.etServiceItemName.text.clear()
        binding.etServiceItemDescription.text.clear()
        binding.etServiceItemTimeLImit.text.clear()
        binding.etCost.text.clear()
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