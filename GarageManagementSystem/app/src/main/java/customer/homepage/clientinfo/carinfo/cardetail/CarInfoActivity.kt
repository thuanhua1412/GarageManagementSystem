package customer.homepage.clientinfo.carinfo.cardetail

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.dataclasses.Car
import com.example.dataclasses.R
import com.example.dataclasses.databinding.ActivityCarInfoBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask


// TODO: To keep the data consistency. When modifying the car infomation. We must also update the
//  services belong to that car.
class CarInfoActivity : FragmentActivity() {
    private lateinit var binding: ActivityCarInfoBinding
    private lateinit var carPassed: Car

    //Code for choosing car image:
    private val pickImageRequest = 1
    private lateinit var carImageUri: Uri
    private var uploadTask: StorageTask<*>? = null
    private val storageRef = FirebaseStorage.getInstance().getReference("CarAvatar")
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_car_info)

        carPassed = intent.extras!!.get("car") as Car
        binding.car = carPassed
        val adapter = CarPagerAdapter(this,carPassed)
//        adapter.addFragmentItem(CarInfoFragment.newInstance(carPassed, "Not used"))
//        adapter.addFragmentItem(CarSpecificServiceFragment.newInstance(1))
        binding.pager.adapter = adapter

        val tabLayout = binding.tabLayout
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when(position){
                0 -> {
                    tab.text = "Thông tin xe"
                }
                1 -> {
                    tab.text = "Các dịch vụ của xe"
                }
            }
        }.attach()
    }


    private inner class CarPagerAdapter(fa: FragmentActivity, private val car: Car) :
        FragmentStateAdapter(fa) {
        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> {
                    CarInfoFragment.newInstance(car, "Not Used")
                }
                1 -> {
                    CarSpecificServiceFragment.newInstance(car,1)
                }
                else -> {
                    CarSpecificServiceFragment.newInstance(car,1)
                }
            }
        }

    }
}


//class ViewPagerAdapter(
//    fragmentManger: FragmentManager,
//    lifecycle: Lifecycle,
//    private val car: Car
//) : FragmentStateAdapter(fragmentManger, lifecycle) {
//    private val fragmentList: ArrayList<Fragment> = ArrayList()
//    fun addFragmentItem(fragment: Fragment) {
//        fragmentList.add(fragment)
//    }
//
//    override fun getItemCount(): Int {
//        return fragmentList.size
//    }
//
//    override fun createFragment(position: Int): Fragment {
////        return fragmentList[position]
//        return when (position) {
//            0 -> {
//                CarInfoFragment.newInstance(car, "Not Used")
//            }
//            1 -> {
//                CarSpecificServiceFragment.newInstance(1)
//            }
//            else ->{
//                CarSpecificServiceFragment.newInstance(1)
//            }
//        }
//    }
//}


//class CarInfoActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityCarInfoBinding
//    private lateinit var carPassed: Car
//
//    //Code for choosing car image:
//    private val pickImageRequest = 1
//    private lateinit var carImageUri: Uri
//    private var uploadTask: StorageTask<*>? = null
//    private val storageRef = FirebaseStorage.getInstance().getReference("CarAvatar")
//    private lateinit var auth: FirebaseAuth
//    private val db = Firebase.firestore
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_car_info)
//
//        carPassed = intent.extras!!.get("car") as Car
//        Toast.makeText(this, "car passed: $carPassed", Toast.LENGTH_SHORT).show()
//        binding.car = carPassed
//        val adapter = ViewPagerAdapter(supportFragmentManager, this.lifecycle, carPassed)
//        adapter.addFragmentItem(CarInfoFragment.newInstance(carPassed, "Not used"))
//        adapter.addFragmentItem(CarSpecificServiceFragment.newInstance(1))
//        binding.pager.adapter = ViewPagerAdapter(supportFragmentManager, this.lifecycle, carPassed)
//    }
//
//}
//
//class ViewPagerAdapter(
//    fragmentManger: FragmentManager,
//    lifecycle: Lifecycle,
//    private val car: Car
//) : FragmentStateAdapter(fragmentManger, lifecycle) {
//    private val fragmentList: ArrayList<Fragment> = ArrayList()
//    fun addFragmentItem(fragment: Fragment) {
//        fragmentList.add(fragment)
//    }
//
//    override fun getItemCount(): Int {
//        return fragmentList.size
//    }
//
//    override fun createFragment(position: Int): Fragment {
//        return fragmentList[position]
//    }
//}


//<?xml version="1.0" encoding="utf-8"?>
//<layout
//xmlns:android="http://schemas.android.com/apk/res/android"
//xmlns:app="http://schemas.android.com/apk/res-auto"
//xmlns:tools="http://schemas.android.com/tools"
//tools:context="customer.homepage.clientinfo.carinfo.cardetail.CarInfoActivity">
//<data>
//<variable
//name="car"
//type="com.example.dataclasses.Car" />
//</data>
//<androidx.constraintlayout.widget.ConstraintLayout
//android:layout_width="match_parent"
//android:layout_height="match_parent"
//android:background="#303030"
//>
//<include
//android:id="@+id/toolbar"
//layout="@layout/toolbar" />
//<androidx.appcompat.widget.LinearLayoutCompat
//android:id="@+id/car_view_box"
//app:layout_constraintStart_toStartOf="parent"
//app:layout_constraintEnd_toEndOf="parent"
//app:layout_constraintTop_toBottomOf="@id/toolbar"
//android:layout_width="match_parent"
//android:layout_height="wrap_content"
//android:orientation="vertical">
//<ImageView
//android:id="@+id/car_image_view"
//android:layout_width="300dp"
//android:layout_height="200dp"
//android:layout_gravity="center_horizontal"
//android:src="@{car.carImageURI}"
//app:error="@{@drawable/car}"/>
//<TextView
//android:id="@+id/change_car_image_text"
//android:layout_width="wrap_content"
//android:layout_height="wrap_content"
//android:text="Thay đổi hình ảnh xe"
//android:gravity="center_vertical"
//android:layout_gravity="center"
//android:padding="6dp"
//android:drawableLeft="@drawable/ic_baseline_edit_24"
//android:clickable="true"
//android:onClick="clickHandler"/>
//
//</androidx.appcompat.widget.LinearLayoutCompat>
//
//<LinearLayout
//android:id="@+id/car_info_box"
//android:layout_width="match_parent"
//android:layout_height="wrap_content"
//android:orientation="vertical"
//app:layout_constraintBottom_toBottomOf="parent"
//app:layout_constraintEnd_toEndOf="parent"
//app:layout_constraintHorizontal_bias="0.0"
//app:layout_constraintStart_toStartOf="parent"
//app:layout_constraintTop_toBottomOf="@id/car_view_box"
//app:layout_constraintVertical_bias="0.0">
//
//<com.google.android.material.textfield.TextInputLayout
//android:id="@+id/filledTextField1"
//
//style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox"
//
//android:layout_width="match_parent"
//android:layout_height="wrap_content"
//android:layout_marginStart="32dp"
//android:layout_marginEnd="32dp"
//android:layout_marginTop="8dp"
//android:layout_marginBottom="8dp"
//android:hint="Car Registration">
//
//<com.google.android.material.textfield.TextInputEditText
//android:id="@+id/car_registration_edit_text"
//android:layout_width="match_parent"
//android:layout_height="wrap_content"
//android:text="@{car.registrationPlate}"
//tools:text="68X4545"/>
//</com.google.android.material.textfield.TextInputLayout>
//
//<com.google.android.material.textfield.TextInputLayout
//android:id="@+id/filledTextField2"
//
//style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox"
//
//android:layout_width="match_parent"
//android:layout_height="wrap_content"
//android:layout_marginStart="32dp"
//android:layout_marginEnd="32dp"
//android:layout_marginTop="8dp"
//android:layout_marginBottom="8dp"
//android:hint="Car Type">
//
//<com.google.android.material.textfield.TextInputEditText
//android:id="@+id/car_type_edit_text"
//android:layout_width="match_parent"
//android:layout_height="wrap_content"
//android:text="@{car.type}"
//tools:text="Ferrari"/>
//</com.google.android.material.textfield.TextInputLayout>
//<com.google.android.material.textfield.TextInputLayout
//android:id="@+id/filledTextField3"
//
//style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox"
//
//android:layout_width="match_parent"
//android:layout_height="wrap_content"
//android:layout_marginStart="32dp"
//android:layout_marginEnd="32dp"
//android:layout_marginTop="8dp"
//android:layout_marginBottom="8dp"
//android:hint="Car Owner">
//
//<com.google.android.material.textfield.TextInputEditText
//android:id="@+id/car_owner_edit_text"
//android:layout_width="match_parent"
//android:layout_height="wrap_content"
//android:text="@{car.ownerID}"
//tools:text="Thinh DEp TRai"/>
//</com.google.android.material.textfield.TextInputLayout>
//
//<Button
//android:id="@+id/service_list_button"
//android:layout_width="wrap_content"
//android:layout_height="wrap_content"
//android:text="jump to servicelist"
//android:layout_gravity="center_horizontal"/>
//<androidx.recyclerview.widget.RecyclerView
//android:layout_width="match_parent"
//android:layout_height="wrap_content"
//android:visibility="gone"/>
//</LinearLayout>
//</androidx.constraintlayout.widget.ConstraintLayout>
//</layout>
//
//
//
//
//
//<?xml version="1.0" encoding="utf-8"?>
//<layout
//xmlns:android="http://schemas.android.com/apk/res/android"
//xmlns:app="http://schemas.android.com/apk/res-auto"
//xmlns:tools="http://schemas.android.com/tools"
//tools:context="customer.homepage.clientinfo.carinfo.cardetail.CarInfoActivity">
//<data>
//<variable
//name="car"
//type="com.example.dataclasses.Car" />
//</data>
//<androidx.constraintlayout.widget.ConstraintLayout
//android:layout_width="match_parent"
//android:layout_height="match_parent"
//android:background="#303030"
//>
//<include
//android:id="@+id/toolbar"
//layout="@layout/toolbar" />
//<androidx.appcompat.widget.LinearLayoutCompat
//android:id="@+id/car_view_box"
//app:layout_constraintStart_toStartOf="parent"
//app:layout_constraintEnd_toEndOf="parent"
//app:layout_constraintTop_toBottomOf="@id/toolbar"
//android:layout_width="match_parent"
//android:layout_height="wrap_content"
//android:orientation="vertical">
//<ImageView
//android:id="@+id/car_image_view"
//android:layout_width="300dp"
//android:layout_height="200dp"
//android:layout_gravity="center_horizontal"
//android:src="@{car.carImageURI}"
//app:error="@{@drawable/car}"/>
//<TextView
//android:id="@+id/change_car_image_text"
//android:layout_width="wrap_content"
//android:layout_height="wrap_content"
//android:text="Thay đổi hình ảnh xe"
//android:gravity="center_vertical"
//android:layout_gravity="center"
//android:padding="6dp"
//android:drawableLeft="@drawable/ic_baseline_edit_24"
//android:clickable="true"
//android:onClick="clickHandler"/>
//
//</androidx.appcompat.widget.LinearLayoutCompat>
//
//<LinearLayout
//android:id="@+id/car_info_box"
//android:layout_width="match_parent"
//android:layout_height="wrap_content"
//android:orientation="vertical"
//app:layout_constraintBottom_toBottomOf="parent"
//app:layout_constraintEnd_toEndOf="parent"
//app:layout_constraintHorizontal_bias="0.0"
//app:layout_constraintStart_toStartOf="parent"
//app:layout_constraintTop_toBottomOf="@id/car_view_box"
//app:layout_constraintVertical_bias="0.0">
//
//<com.google.android.material.textfield.TextInputLayout
//android:id="@+id/filledTextField1"
//
//style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox"
//
//android:layout_width="match_parent"
//android:layout_height="wrap_content"
//android:layout_marginStart="32dp"
//android:layout_marginEnd="32dp"
//android:layout_marginTop="8dp"
//android:layout_marginBottom="8dp"
//android:hint="Car Registration">
//
//<com.google.android.material.textfield.TextInputEditText
//android:id="@+id/car_registration_edit_text"
//android:layout_width="match_parent"
//android:layout_height="wrap_content"
//android:text="@{car.registrationPlate}"
//tools:text="68X4545"/>
//</com.google.android.material.textfield.TextInputLayout>
//
//<com.google.android.material.textfield.TextInputLayout
//android:id="@+id/filledTextField2"
//
//style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox"
//
//android:layout_width="match_parent"
//android:layout_height="wrap_content"
//android:layout_marginStart="32dp"
//android:layout_marginEnd="32dp"
//android:layout_marginTop="8dp"
//android:layout_marginBottom="8dp"
//android:hint="Car Type">
//
//<com.google.android.material.textfield.TextInputEditText
//android:id="@+id/car_type_edit_text"
//android:layout_width="match_parent"
//android:layout_height="wrap_content"
//android:text="@{car.type}"
//tools:text="Ferrari"/>
//</com.google.android.material.textfield.TextInputLayout>
//<com.google.android.material.textfield.TextInputLayout
//android:id="@+id/filledTextField3"
//
//style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox"
//
//android:layout_width="match_parent"
//android:layout_height="wrap_content"
//android:layout_marginStart="32dp"
//android:layout_marginEnd="32dp"
//android:layout_marginTop="8dp"
//android:layout_marginBottom="8dp"
//android:hint="Car Owner">
//
//<com.google.android.material.textfield.TextInputEditText
//android:id="@+id/car_owner_edit_text"
//android:layout_width="match_parent"
//android:layout_height="wrap_content"
//android:text="@{car.ownerID}"
//tools:text="Thinh DEp TRai"/>
//</com.google.android.material.textfield.TextInputLayout>
//
//<Button
//android:id="@+id/service_list_button"
//android:layout_width="wrap_content"
//android:layout_height="wrap_content"
//android:text="jump to servicelist"
//android:layout_gravity="center_horizontal"/>
//<androidx.recyclerview.widget.RecyclerView
//android:layout_width="match_parent"
//android:layout_height="wrap_content"
//android:visibility="gone"/>
//</LinearLayout>
//</androidx.constraintlayout.widget.ConstraintLayout>
//</layout>


//
//class CarInfoActivity : AppCompatActivity() {
//    private lateinit var binding : ActivityCarInfoBinding
//    private lateinit var carPassed: Car
//
//    //Code for choosing car image:
//    private val pickImageRequest = 1
//    private lateinit var carImageUri: Uri
//    private var uploadTask: StorageTask<*>? = null
//    private val storageRef = FirebaseStorage.getInstance().getReference("CarAvatar")
//    private lateinit var auth: FirebaseAuth
//    private val db = Firebase.firestore
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = DataBindingUtil.setContentView(this,R.layout.activity_car_info)
//
//        carPassed = intent.extras!!.get("car") as Car
//        Toast.makeText(this, "car passed: $carPassed", Toast.LENGTH_SHORT).show()
//        binding.car = carPassed
//
//
//        disableEditText(binding.carOwnerEditText)
//        binding.serviceListButton.setOnClickListener {
//            navigateToCarServiceList()
//        }
//        //        val changeCarInfoButton = findViewById<Button>(R.id.button)
////        changeCarInfoButton.setOnClickListener{
////            val intent = Intent (this, changeCarInfo::class.java)
////            startActivity(intent)
////        }
////
////        val deleteCarInfoButton = findViewById<Button>(R.id.button2)
////        deleteCarInfoButton.setOnClickListener{
////            val intent = Intent (this, CarListActivity::class.java)
////            startActivity(intent)
////        }
//
//    }
//
//    fun clickHandler(view: View) {
//        when(view.id){
//            R.id.change_car_image_text ->{
//                openFileChooser()
//            }
//        }
//    }
//
//    private fun navigateToCarServiceList(){
////        val intent = Intent(this,CarInfoFragment::class.java)
////        intent.extras!!.putParcelable("car",carPassed)
////        startFramen
//    }
//
//
//    private fun disableEditText(editText: EditText) {
//        editText.isFocusable = false
//        editText.isEnabled = false
//        editText.isCursorVisible = false
//        editText.keyListener = null
//        editText.setBackgroundColor(Color.TRANSPARENT)
//    }
//
//    private fun updateCarInfo() {
//        if (validateUserChoice()) {
//            val updatedCar = carPassed
//            var carURL: String = "null"
//            val fileRef = storageRef.child(
//                "${System.currentTimeMillis()}.${
//                    getFileExtension(
//                        carImageUri
//                    )
//                }"
//            )
//            uploadTask = fileRef.putFile(carImageUri)
//                .addOnSuccessListener {
//                    val imageURL = fileRef.downloadUrl
//                        .addOnSuccessListener {
//                            carURL = it.toString()
//                            Log.d("Image Uploaded URL:", carURL)
//                            // Repare Updated user for update
//                            updatedCar.carImageURI = carURL
//                            updatedCar.registrationPlate = binding.carRegistrationEditText.text.toString()
//                            updatedCar.type = binding.carTypeEditText.text.toString()
//                            // Find the old user's Document ID
//                            db.collection("Car")
//                                .whereEqualTo("registrationPlate", carPassed.registrationPlate)
//                                .limit(1)
//                                .get()
//                                .addOnSuccessListener {
//                                    val carID = it.documents[0].id
//                                    db.collection("Car")
//                                        .document(carID)
//                                        .set(updatedCar)
//                                        .addOnSuccessListener {
//                                            Log.d("UPDATED_CAR:","$updatedCar")
//                                            Toast.makeText(this, "Car Updated succesfully", Toast.LENGTH_SHORT).show()
//                                            binding.car = updatedCar
//                                            carPassed = updatedCar
//                                        }
//                                        .addOnFailureListener{
//                                            Toast.makeText(this, "Thay đổi thông tin thất bại, ${it.message}", Toast.LENGTH_SHORT).show()
//                                        }
//                                }
//                        }
//                        .addOnFailureListener {
//                            Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT)
//                                .show()
//                        }
//                }
//                .addOnProgressListener {
//                }
//                .addOnFailureListener {
//                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
//                }
//        }
//    }
//
//    private fun validateUserChoice():Boolean{
//        val regNum = binding.carRegistrationEditText.text.toString()
//        val carType = binding.carTypeEditText.text.toString()
//        return if(TextUtils.isEmpty(regNum) || (TextUtils.isEmpty(carType))){
//            Toast.makeText(this, "Missing Required fields", Toast.LENGTH_SHORT).show()
//            false
//        } else true
//    }
//
//    // Code for picking image and uploading the image to Firebase Storage
//    private fun openFileChooser() {
//        val intent = Intent()
//        intent.type = "image/*"
//        intent.action = Intent.ACTION_GET_CONTENT
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), pickImageRequest)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        // Check if the user actually picked an image and everything went well
//        if (requestCode == pickImageRequest && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
//            carImageUri = data.data!!
//            binding.carImageView.setImageURI(carImageUri)
//        }
//    }
//
//    private fun getFileExtension(uri: Uri): String? {
//        val contentResolver = contentResolver
//        val mime = MimeTypeMap.getSingleton()
//        return mime.getExtensionFromMimeType(contentResolver.getType(uri))
//    }
//
//
//}