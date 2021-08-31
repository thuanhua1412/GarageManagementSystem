package customer.homepage.clientinfo.carinfo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.dataclasses.Car
import com.example.dataclasses.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask


// TODO: Implement image chooser
@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AddCarActivity : AppCompatActivity() {

    //Code for choosing car image:
    private val pickImageRequest = 1
    private lateinit var chooseImageButton: Button
    private lateinit var carImageView: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var carImageUri: Uri
    private var uploadTask: StorageTask<*>? = null
    private val storageRef = FirebaseStorage.getInstance().getReference("CarImage")

    private lateinit var addCarButton: FloatingActionButton
    private lateinit var carBranchTypeEditText: EditText
    private lateinit var carRegistrationPlateEditText: EditText

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_car)

        initializeUI()
    }

    // Code for picking image and uploading the image to Firebase Storage
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
            carImageUri = data.data!!
            carImageView.setImageURI(carImageUri)
        }
    }

    private fun getFileExtension(uri: Uri): String? {
        val contentResolver = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    private fun initializeUI() {
        addCarButton = findViewById(R.id.finish_add_car_button)
        carBranchTypeEditText = findViewById(R.id.car_type_edit_text)
        carRegistrationPlateEditText = findViewById(R.id.registration_plate_edit_text)
        chooseImageButton = findViewById(R.id.choose_car_image_button)
        carImageView = findViewById(R.id.car_image_view)
        progressBar = findViewById(R.id.progress_image_car)

        // Assigning onclick Listeners
        chooseImageButton.setOnClickListener {
            openFileChooser()
        }

        addCarButton.setOnClickListener {
            if (validCarInfo()) {
                addNewCarToFireStore()
            }
        }
    }

    // TODO: Create a image picker for the car and then check if an image was chosen
    private fun validCarInfo(): Boolean {
        if (TextUtils.isEmpty(carRegistrationPlateEditText.text.toString()) || (TextUtils.isEmpty(
                carBranchTypeEditText.text.toString()
            ))
        ) {
            Toast.makeText(this, "Some field is missing", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    // TODO: Write cleaner code by creating options to add new Component to car
    private fun addNewCarToFireStore() {
        val registrationNum = carRegistrationPlateEditText.text.toString()
        val type = carBranchTypeEditText.text.toString()
        var carImageURI = "null" // Need to implement image chooser
        val ownerID = currentUser.email
        val fileRef = storageRef.child(
            "${System.currentTimeMillis()}.${
                getFileExtension(
                    carImageUri
                )
            }"
        )
        uploadTask = fileRef.putFile(carImageUri)
            .addOnSuccessListener {
                val imageURL = fileRef.downloadUrl
                    .addOnSuccessListener {
                        carImageURI = it.toString()
                        Log.d("Image Uploaded URL:", carImageURI)
                        val carToAdd = Car(
                            registrationPlate = registrationNum,
                            type = type,
                            ownerID = ownerID,
                            carImageURI = carImageURI
                        )
                        val car = hashMapOf(
                            "registrationPlate" to registrationNum,
                            "type" to type,
                            "ownerID" to ownerID,
                            "carImageURI" to carImageURI
                        )
                        db.collection("Car")
                            .add(car)
                            .addOnSuccessListener { carDocument ->
                                Toast.makeText(this, "Car succesfully added", Toast.LENGTH_SHORT).show()
                                Log.d("CAR ADDER", "Car's Document ID: ${carDocument.id}")
//                                val carComponentRef =
//                                    db.collection("Car").document(carDocument.id).collection("Component")
//                                for (component in carToAdd.components) {
//                                    carComponentRef.add(component)
//                                        .addOnSuccessListener {
//                                            Log.d("ADD CAR COMPONENT:", component.name)
//                                        }
//                                }
                                navigateBackToCarList()
                            }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnProgressListener {
                val progress = (100.0 * it.bytesTransferred / it.totalByteCount)
                progressBar.progress = progress.toInt()
            }
            .addOnFailureListener {
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }


    }


    private fun navigateBackToCarList(){
        val intent = Intent(this, CarListActivity::class.java)
        startActivity(intent)
    }


}