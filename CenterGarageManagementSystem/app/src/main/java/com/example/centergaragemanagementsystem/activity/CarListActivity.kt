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
import com.example.centergaragemanagementsystem.adapter.CarItemAdapter
import com.example.centergaragemanagementsystem.adapter.ServiceClickListener
import com.example.centergaragemanagementsystem.dataClass.SERVICE_EXECUTING
import com.example.centergaragemanagementsystem.databinding.ActivityCarListBinding

class CarListActivity : AppCompatActivity() {
    private lateinit var adapter : CarItemAdapter
    //private lateinit var binding : ActivityNhanXeBinding//ActivityCarlistBinding
    private lateinit var binding: ActivityCarListBinding
    private lateinit var serviceViewModel : ServiceViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding = DataBindingUtil.setContentView(this,R.layout.activity_nhan_xe)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_car_list)

        serviceViewModel = ViewModelProvider(this).get(ServiceViewModel()::class.java)

        showSuitableCar()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = "Danh sách xe đang sửa chữa"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun showSuitableCar() {


        serviceViewModel.getMergedLiveData().observe(this, Observer { triple ->
            val filteredServiceList = triple.first.filter{serviceItem ->
                serviceItem.status == SERVICE_EXECUTING
            }

            Log.d("FILTER_ARRAY_LIST","$filteredServiceList")
            adapter = CarItemAdapter(ArrayList(filteredServiceList)
                , triple.second
                , ServiceClickListener {
                    Toast.makeText(this, "${it.serviceID} is clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ServiceListActivity::class.java)
                    intent.putExtra("ownerID", it.ownerID)
                    intent.putExtra("serviceID", it.serviceID)
                    intent.putExtra("carServiced", it.carServiced)
                    intent.putExtra("cartID", it.cartID)


                    startActivity(intent)
                    // Make Editext constant
                }, serviceViewModel)
            binding.carlistRecycleView.adapter = adapter
            binding.carlistRecycleView.layoutManager = LinearLayoutManager(this)
        })
    }

    private fun loadNextPage() {
        val intent = Intent(this, ServiceListActivity::class.java)
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