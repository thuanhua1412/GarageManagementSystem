package com.example.centergaragemanagementsystem.activity

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
import com.example.centergaragemanagementsystem.adapter.ServiceListAdapter
import com.example.centergaragemanagementsystem.dataClass.SERVICE_CONFIRMATION_WAITING
import com.example.centergaragemanagementsystem.dataClass.SERVICE_WAITING
import com.example.centergaragemanagementsystem.databinding.ActivityConfirmServiceBinding
import com.example.centergaragemanagementsystem.viewmodel.ServiceViewModel

class ConfirmServiceActivity : AppCompatActivity() {
    private lateinit var binding : ActivityConfirmServiceBinding
    private lateinit var adapter : ServiceListAdapter
    private lateinit var serviceViewModel : ServiceViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_confirm_service)

        serviceViewModel = ViewModelProvider(this).get(ServiceViewModel()::class.java)

        displayOnRecyclerView()


        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = "Lịch hẹn chờ duyệt"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

    }

    private fun displayOnRecyclerView() {
        serviceViewModel.getMergedLiveData().observe(this, Observer { triple ->
            val filteredServiceList = triple.first.filter { serviceItem ->
                serviceItem.status == SERVICE_CONFIRMATION_WAITING
            }

            Log.d("FILTER_ARRAY_LISTTT", "$filteredServiceList")

            adapter = ServiceListAdapter(ArrayList(filteredServiceList), triple.second, ServiceClickListener {},
                ServiceClickListener{ service ->
                    val filteredService = triple.first.filter {
                        service.cartID == it.cartID
                    }
                    for (filter in filteredService) {
                        serviceViewModel.updateServiceStatus(filter.serviceID, SERVICE_WAITING)
                    }
                    Toast.makeText(this,"Service Confirmed", Toast.LENGTH_SHORT).show()

                })
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
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