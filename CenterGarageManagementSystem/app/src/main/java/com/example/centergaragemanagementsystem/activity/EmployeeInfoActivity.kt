package com.example.centergaragemanagementsystem.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.centergaragemanagementsystem.R
import com.example.centergaragemanagementsystem.ViewModel.EmployeeViewModel
import com.example.centergaragemanagementsystem.databinding.ActivityEmployeeInfoBinding
import login.LoginActivity

class EmployeeInfoActivity : AppCompatActivity() {
    private lateinit var  employeeViewModel : EmployeeViewModel
    private lateinit var binding : ActivityEmployeeInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_employee_info)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = "Thông tin cá nhân"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        employeeViewModel = ViewModelProvider(this).get(EmployeeViewModel::class.java)

        employeeViewModel.getUserLiveData().observe(this, Observer { user ->
            binding.employee = user
        })

        binding.basicProfile.setOnClickListener {
            loadDetailInfoPage()
        }


        binding.logout.setOnClickListener {
            loadLoginPage()
        }
    }


    private fun loadLoginPage() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    private fun loadDetailInfoPage() {
        val intent = Intent(this, EmployeeInfoDetailActivity::class.java)
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