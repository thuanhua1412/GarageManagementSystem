package com.example.centergaragemanagementsystem.activity

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.centergaragemanagementsystem.R
import com.example.centergaragemanagementsystem.viewmodel.ServiceViewModel
import com.example.centergaragemanagementsystem.adapter.CarPhotoAdapter
import com.example.centergaragemanagementsystem.dataClass.Car
import com.example.centergaragemanagementsystem.dataClass.Customer
import com.example.centergaragemanagementsystem.dataClass.Service
import com.example.centergaragemanagementsystem.databinding.ActivityNhanXeCarInfoBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream


class NhanXeCarInfoActivity : AppCompatActivity() {
    private val cameraActionCode = 1
    private lateinit var adapter: CarPhotoAdapter
    private lateinit var serviceID:String
    private lateinit var allCurrentService: ArrayList<Service>
    private lateinit var cartID:String
    private lateinit var ImageUri: Uri
    private lateinit var binding : ActivityNhanXeCarInfoBinding
    private lateinit var serviceViewModel: ServiceViewModel
    private val storageRef = FirebaseStorage.getInstance().getReference("Service")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_nhan_xe_car_info)
        InitializeUI()
        val toolbar:Toolbar = findViewById(R.id.toolbar)
        toolbar.title = "Thông tin xe"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        if (!intent.getStringExtra("newCustomer").toBoolean()) {
            displayCarInfo()
        }

        binding.imageButton.setOnClickListener{
            takeAPhoto()
        }
        binding.btnIncrease.setOnClickListener{
            val speedometer = binding.etSpeedometer.text.toString().toIntOrNull()
            if (speedometer != null) {
                binding.etSpeedometer.setText((speedometer + 1).toString())
            }
            else {
                Toast.makeText(this, "Speedometer must be numberic, Please try again!", Toast.LENGTH_SHORT).show()
            }

        }
        binding.btnDecrease.setOnClickListener{
            val speedometer = binding.etSpeedometer.text.toString().toIntOrNull()
            if (speedometer != null) {
                if (speedometer > 0) {
                    binding.etSpeedometer.setText((speedometer - 1).toString())
                }
                else {
                    Toast.makeText(this, "Speedometer cannot be negative, Please try again!", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(this, "Speedometer must be numberic, Please try again!", Toast.LENGTH_SHORT).show()
            }


        }

        binding.btnContinue.setOnClickListener {
            if (intent.getStringExtra("newCustomer").toBoolean()) {
                addNewCarToFirestore()
            }

            Log.d("asdfafs", "${binding.etItemOnCar.text}, ${binding.etSpeedometer.text}")
            if (binding.etItemOnCar.text.toString() != "" && binding.etSpeedometer.text.toString() != "0") {
                for (currentService in allCurrentService) {
                    serviceViewModel.updateCarServicedInfo(currentService.serviceID, binding.etSpeedometer.text.toString(), binding.etItemOnCar.text.toString())
                }
                if (intent.getStringExtra("newCustomer").toBoolean()) {
                    loadHomePage()
                }
                else {
                    loadNextPage()
                }
            }
            else {
                Toast.makeText(this, "Speedometer and Item cannot be empty, Please try again!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun loadHomePage() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // clean up all activity
        startActivity(intent)
        finish()
    }

    private fun addNewCarToFirestore() {
        val registrationNum = binding.etCarReg.text.toString()
        val type = binding.etCarModel.text.toString()
        if(validateCarInfoInput(registrationNum, type)) {
            val customerID = intent.getStringExtra("customerID").toString()

            Firebase.firestore.collection("Customer").document(customerID).get().addOnSuccessListener {
                val customer = it.toObject(Customer::class.java)
                val ownerID = customer!!.email
                val car = Car(registrationPlate = registrationNum, type = type, ownerID = ownerID)
                Firebase.firestore.collection("Car").add(car)
                    .addOnSuccessListener { carDocument ->
                        Toast.makeText(this, "Car succesfully added", Toast.LENGTH_SHORT).show()
                        Log.d("CAR ADDER", "Car's Document ID: ${carDocument.id}")
                    }
            }
        }
    }

    private fun validateCarInfoInput(registrationNum: String, type: String) : Boolean {
        if (registrationNum == "" || type == "") {
            Toast.makeText(this, "All field can't null", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun InitializeUI() {
        serviceViewModel = ViewModelProvider(this).get(ServiceViewModel()::class.java)
        if (intent.extras != null) {
            serviceID = intent.getStringExtra("serviceID").toString()
            cartID = intent.getStringExtra("cartID").toString()
            serviceViewModel.getServiceListLiveData().observe(this, Observer { serviceList ->
//                val currentService = serviceList.find {
//                    it.serviceID == serviceID
//                }
                allCurrentService = ArrayList(serviceList.filter {
                    it.cartID == cartID
                })
                Log.d("NhanXeCarInfoActivityCurrentService: ", "${allCurrentService[0]}, $serviceID")
                adapter = CarPhotoAdapter(allCurrentService[0])
                binding.viewpager.adapter = adapter
                binding.indicator.setViewPager(binding.viewpager)
            })
        }
    }

    private fun takeAPhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        try {
            startActivityForResult(intent, cameraActionCode)
        } catch (e: ActivityNotFoundException){
            Toast.makeText(this, "there is no app support this action", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("TAKEAPHOTORESULT1", "$requestCode, $resultCode, $data, ${data?.data}")
        if (requestCode == cameraActionCode && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("TAKEAPHOTORESULT", "successful")
            val extras = data.extras
            val imageBitmap:Bitmap = extras?.get("data") as Bitmap
            val uri = getImageUri(imageBitmap)
            ImageUri = uri
            addPictureToFirestore()
        }
    }

    private fun getImageUri(imageBitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String = MediaStore.Images.Media.insertImage(
            applicationContext.contentResolver, imageBitmap, "Title", null)
        return Uri.parse(path)
    }

    private fun addPictureToFirestore() {
        var imageURI : String
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
                    imageURI = it.toString()
                    Log.d("Image Uploaded URL:", imageURI)
                    for (currentService in allCurrentService) {
                        serviceViewModel.updateServicePhoto(currentService.serviceID, imageURI)
                    }

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

    private fun displayCarInfo() {
        val carServiced = intent.getStringExtra("carServiced").toString()
        Log.d("CARSERVICED", "$carServiced ")
        serviceViewModel = ViewModelProvider(this).get(ServiceViewModel()::class.java)
        serviceViewModel.getMergedLiveData().observe(this, Observer { triple ->
            val filteredCar = triple.second.find {
                it.registrationPlate == carServiced
            }
            binding.car = filteredCar
        })
        binding.etCarModel.isEnabled = false
        binding.etCarReg.isEnabled = false
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
    private fun loadNextPage() {
        val intent = Intent(this, ArisingServiceActivity::class.java)
        intent.putExtra("serviceID", serviceID)
        intent.putExtra("cartID", cartID)
        intent.putExtra("newCustomer", "false")
        startActivity(intent)
    }


}
