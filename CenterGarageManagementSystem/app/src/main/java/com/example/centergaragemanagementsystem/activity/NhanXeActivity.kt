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
import com.example.centergaragemanagementsystem.viewmodel.ServiceViewModel
import com.example.centergaragemanagementsystem.adapter.ServiceClickListener
import com.example.centergaragemanagementsystem.adapter.ServiceListAdapter
import com.example.centergaragemanagementsystem.dataClass.SERVICE_WAITING
import com.example.centergaragemanagementsystem.databinding.ActivityNhanXeBinding
import java.util.*

class NhanXeActivity : AppCompatActivity() {
    private lateinit var adapter : ServiceListAdapter
    private lateinit var binding : ActivityNhanXeBinding
    private lateinit var serviceViewModel : ServiceViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_nhan_xe)

        serviceViewModel = ViewModelProvider(this).get(ServiceViewModel()::class.java)


        binding.findButton.setOnClickListener {
            showSuitableService()
        }

        binding.newCustomerButton.setOnClickListener {
            loadNextPage()
        }
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = "Nhận xe"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun showSuitableService() {
        val rego = binding.customerCarRego.text.toString()
        Log.d("NAME_AND_REGO:",rego)
        serviceViewModel.getMergedLiveData().observe(this, Observer { triple ->
            val filteredServiceList = triple.first.filter{serviceItem ->
                serviceItem.carServiced.contains(rego)
                        && serviceItem.status == SERVICE_WAITING
                        && serviceItem.fromClient
            }

            Log.d("FILTER_ARRAY_LIST","$filteredServiceList")
            adapter = ServiceListAdapter(
                ArrayList(filteredServiceList)
                , triple.second
                , ServiceClickListener {
                    Toast.makeText(this, "${it.cartID} is clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, NhanXeCustomerInfoActivity::class.java)
                    intent.putExtra("ownerID", it.ownerID)
                    intent.putExtra("serviceID", it.serviceID)
                    intent.putExtra("carServiced", it.carServiced)
                    intent.putExtra("cartID", it.cartID)
                    intent.putExtra("new customer", false)

                    startActivity(intent)
                    // Make Editext constant
                }, ServiceClickListener {  }, true)
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
        })
    }

    private fun loadNextPage() {
        val intent = Intent(this, NhanXeCustomerInfoActivity::class.java)
        intent.putExtra("newCustomer", "true")
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
