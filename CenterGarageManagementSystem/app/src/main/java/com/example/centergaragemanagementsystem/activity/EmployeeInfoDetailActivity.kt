package com.example.centergaragemanagementsystem.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.centergaragemanagementsystem.ViewModel.EmployeeViewModel
import com.example.centergaragemanagementsystem.R
import com.example.centergaragemanagementsystem.databinding.ActivityEmployeeInfoDetailBinding

class EmployeeInfoDetailActivity : AppCompatActivity() {
    private lateinit var  employeeViewModel : EmployeeViewModel
    private lateinit var binding : ActivityEmployeeInfoDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_employee_info_detail)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = "Thông tin cơ bản"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        employeeViewModel = ViewModelProvider(this).get(EmployeeViewModel::class.java)

        employeeViewModel.getUserLiveData().observe(this, Observer { user ->
            binding.employee = user
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