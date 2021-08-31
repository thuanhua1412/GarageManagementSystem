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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.centergaragemanagementsystem.R
import com.example.centergaragemanagementsystem.adapter.ServiceClickListener
import com.example.centergaragemanagementsystem.adapter.ServiceItemAdapter
import com.example.centergaragemanagementsystem.dataClass.SERVICE_EXECUTING
import com.example.centergaragemanagementsystem.dataClass.SERVICE_FINISHED
import com.example.centergaragemanagementsystem.databinding.ActivityCarinfoBinding
import com.example.centergaragemanagementsystem.viewmodel.ServiceViewModel

class ServiceListActivity : AppCompatActivity() {
    private lateinit var adapter : ServiceItemAdapter
    //private lateinit var binding : ActivityNhanXeBinding//ActivityCarlistBinding
    private lateinit var binding: ActivityCarinfoBinding
    private lateinit var serviceViewModel : ServiceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding = DataBindingUtil.setContentView(this,R.layout.activity_nhan_xe)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_carinfo)

        serviceViewModel = ViewModelProvider(this).get(ServiceViewModel()::class.java)



        showSuitableService()
        if(intent.extras != null){
            displayServiceList()
        }
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = "Thông tin chi tiết"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun displayServiceList() {
        //TODO("Not yet implemented")
        val ownerID = intent.getStringExtra("ownerID").toString()
        val cartID = intent.getStringExtra("cartID").toString()
        val carServiced = intent.getStringExtra("carServiced").toString()
        Log.d("OWNERID", "$ownerID ")
        Log.d("SERVICEID", "$carServiced")
        Toast.makeText(this, "Service Check Extra Toast: $ownerID", Toast.LENGTH_SHORT).show()

        serviceViewModel = ViewModelProvider(this).get(ServiceViewModel()::class.java)
        serviceViewModel.getMergedLiveData().observe(this, Observer { triple ->
            val filteredCustomer = triple.third.find {
                it.email == ownerID
            }
            val filteredService = triple.first.find {
                it.status == SERVICE_EXECUTING && it.ownerID == ownerID && it.cartID == cartID
            }
            val filteredCar = triple.second.find{
                it.ownerID == ownerID
            }

            binding.car = filteredCar
            binding.service = filteredService

        })
    }

    private fun showSuitableService() {
        val ownerID = intent.getStringExtra("ownerID").toString()
        val cartID = intent.getStringExtra("cartID").toString()
        serviceViewModel.getMergedLiveData().observe(this, Observer { triple ->
            val filteredServiceList = triple.first.filter{
                it.status == SERVICE_EXECUTING && it.ownerID == ownerID
//                (it.status == SERVICE_EXECUTING || it.status == SERVICE_FINISHED) && it.ownerID == ownerID
            }

            Log.d("FILTER_ARRAY_LIST_Test","$filteredServiceList  $ownerID   $cartID")
            adapter = ServiceItemAdapter(ArrayList(filteredServiceList)
                , triple.second
                , ServiceClickListener {service ->
                    Toast.makeText(this, "${service.serviceID} is choosen", Toast.LENGTH_SHORT).show()

                    serviceViewModel.updateServiceStatus(service.serviceID, SERVICE_FINISHED)

                    Toast.makeText(this,"DONE!!", Toast.LENGTH_SHORT).show()

                    // Make Editext constant
                }, serviceViewModel)
            binding.confirmButton.setOnClickListener{
                for (ser in filteredServiceList) {
                    if(ser.status == SERVICE_EXECUTING) {
                        serviceViewModel.updateServiceStatus(ser.serviceID, SERVICE_FINISHED)
                    }
                }
                Toast.makeText(this, "All done", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, CarListActivity::class.java)
                startActivity(intent)
            }
            binding.servicelistRecycleView.adapter = adapter
            binding.servicelistRecycleView.layoutManager = LinearLayoutManager(this)
        })
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