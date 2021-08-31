package register

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.dataclasses.Customer
import com.example.dataclasses.R
import com.example.dataclasses.databinding.ActivityRegisterEndBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import login.LoginActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class RegisterEndActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterEndBinding


    private val pickImageRequest = 1
    private lateinit var avatarImageUri: Uri
    private var uploadTask: StorageTask<*>? = null
    private val storageRef = FirebaseStorage.getInstance().getReference("UserAvatar")
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore


    private lateinit var passedCustomer: Customer
    private lateinit var password: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_end)

        binding.buttonChooseImage.setOnClickListener {
            openFileChooser()
        }

        binding.buttonRegister.setOnClickListener {
            registerNewUser()
        }

        passedCustomer = intent.extras?.getParcelable<Customer>("customer")!!
        password = intent.extras!!.getString("password").toString()


    }

    fun onClockClick(view: View) {
        when (view.id) {
            R.id.birthdate_clock -> {
                pickDateTime()
            }
            else -> {

            }
        }
    }

    private fun registerNewUser() {
        if (validateUserChoice()) {
            val email = passedCustomer.email
            var avatarURL: String = "null"
            val fileRef = storageRef.child(
                "${System.currentTimeMillis()}.${
                    getFileExtension(
                        avatarImageUri
                    )
                }"
            )
            uploadTask = fileRef.putFile(avatarImageUri)
                .addOnSuccessListener {
                    val imageURL = fileRef.downloadUrl
                        .addOnSuccessListener {
                            avatarURL = it.toString()
                            Log.d("Image Uploaded URL:", avatarURL)
                            passedCustomer.avatarImageUri = avatarURL
                            passedCustomer.dateOfBirth = binding.birthDayEditText.text.toString()
                            auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        db.collection("Customer")
                                            .add(passedCustomer)
                                            .addOnSuccessListener { customerDocument ->
                                                Toast.makeText(
                                                    this,
                                                    "Customer succesfully added",
                                                    Toast.LENGTH_SHORT
                                                )
                                                    .show()
                                                Log.d(
                                                    "CUSTOMER ADDER",
                                                    "Customer's Document ID: ${customerDocument.id}"
                                                )
                                                loadLoginScreen()
                                            }
                                            .addOnFailureListener {
                                                Toast.makeText(
                                                    this,
                                                    "failed to create user, ${it.message}",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                    }
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
                .addOnProgressListener {
                }
                .addOnFailureListener {
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
    private fun validateUserChoice(): Boolean {
        val birthDate = binding.birthDayEditText.text.toString()
        return if (TextUtils.isEmpty(birthDate) || !(isValidDate(birthDate))) {
            Toast.makeText(this, "Please enter a valid birthdate", Toast.LENGTH_SHORT).show()
            false
        }
        else if (!binding.termCheckBox.isChecked){
            Toast.makeText(this, "Không đồng ý thì cút", Toast.LENGTH_SHORT).show()
            false
        }
        else {
            true
        }

    }

    private fun loadLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("username", passedCustomer.email)
        intent.putExtra("password", password)
        startActivity(intent)
    }

    private fun pickDateTime() {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)


        DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, day ->
            var dateText = "$day/${month + 1}/$year"
            binding.birthDayEditText.setText(dateText)
        }, startYear, startMonth, startDay).show()
    }

    @SuppressLint("SimpleDateFormat")
    private fun isValidDate(value: String?): Boolean {
        return try {
            SimpleDateFormat("dd/mm/yyyy").parse(value)
            true
        } catch (e: ParseException) {
            false
        }
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
            avatarImageUri = data.data!!
            binding.avatarImage.setImageURI(avatarImageUri)
        }
    }

    private fun getFileExtension(uri: Uri): String? {
        val contentResolver = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(contentResolver.getType(uri))
    }

}

