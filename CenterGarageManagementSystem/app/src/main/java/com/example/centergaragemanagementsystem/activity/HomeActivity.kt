package com.example.centergaragemanagementsystem.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.centergaragemanagementsystem.R
import com.example.centergaragemanagementsystem.ViewModel.EmployeeViewModel
import com.example.centergaragemanagementsystem.dataClass.Manager
import com.example.centergaragemanagementsystem.databinding.ActivityHomeBinding
import com.google.firebase.storage.FirebaseStorage

class HomeActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private lateinit var  employeeViewModel : EmployeeViewModel
    private lateinit var binding : ActivityHomeBinding
    private val pickImageRequest = 1
    private val storageRef = FirebaseStorage.getInstance().getReference("UserAvatar")
    private lateinit var ImageUri: Uri
    private lateinit var currentUser:Manager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        initializeUI()
        val toolbar:Toolbar = findViewById(R.id.toolbar)

        toolbar.title = "Trang chủ"
        setSupportActionBar(toolbar)

        binding.btnProfile.setOnClickListener {
            val intent = Intent(this, EmployeeInfoActivity::class.java)
            startActivity(intent)
        }

        binding.ibAvatar.setOnClickListener {
            openFileChooser()
        }

        binding.btnNhanXe.setOnClickListener {
            val intent = Intent(this, NhanXeActivity::class.java)
            startActivity(intent)
        }

        binding.xeDangSuaButton.setOnClickListener{
            val intent = Intent(this, CarListActivity::class.java)
            startActivity(intent)
        }

        binding.btnLichHen.setOnClickListener {
            val intent = Intent(this, ConfirmServiceActivity::class.java)
            startActivity(intent)
        }

        binding.customerButton.setOnClickListener{
            val intent = Intent(this, CustomerListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun addPictureToFirestore() {
        var ImageURI : String
        val fileRef = storageRef.child(
            "${System.currentTimeMillis()}.${
                getFileExtension(
                    ImageUri
                )
            }"
        )

        val uploadTask = fileRef.putFile(ImageUri).
        addOnSuccessListener {
            val imageURL = fileRef.downloadUrl
                .addOnSuccessListener {
                    ImageURI = it.toString()
                    Log.d("Image Uploaded URL:", ImageURI)
                    employeeViewModel.UpdateUserImage(currentUser, ImageURI)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun getFileExtension(uri: Uri): String? {
        val contentResolver = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(contentResolver.getType(uri))
    }


    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), pickImageRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Check if the user actually picked an image and everything went well
        if (requestCode == pickImageRequest && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            ImageUri = data.data!!
            addPictureToFirestore()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initializeUI() {
        employeeViewModel = ViewModelProvider(this).get(EmployeeViewModel::class.java)

        employeeViewModel.getUserLiveData().observe(this, Observer { user ->
            binding.employee = user
            currentUser = user
        })
    }

    override fun onBackPressed() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        startActivity(intent)
    }
}