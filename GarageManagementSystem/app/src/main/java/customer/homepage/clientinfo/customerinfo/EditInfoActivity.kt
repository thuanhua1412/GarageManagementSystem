package customer.homepage.clientinfo.customerinfo

import repository.Repository
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.dataclasses.R
import com.example.dataclasses.databinding.ActivityEditInfoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import customer.homepage.clientinfo.ClientInfoFragment
import java.text.ParseException
import java.text.SimpleDateFormat


class EditInfoActivity : AppCompatActivity() {

    //Code for choosing car image:
    private val pickImageRequest = 1
    private lateinit var avatarImageUri: Uri
    private var uploadTask: StorageTask<*>? = null
    private val storageRef = FirebaseStorage.getInstance().getReference("UserAvatar")
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    private lateinit var binding: ActivityEditInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Init Layout code
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_info)
        binding.customer = Repository.customer
        auth = FirebaseAuth.getInstance()

        initUI()



    }


    private fun initUI(){
        val customer = Repository.customer
        binding.birthDateEditText.setText(customer.dateOfBirth)
        binding.nameEditText.setText(customer.name)
        binding.phoneNumberEditText.setText(customer.phone)
        binding.updateInfoButton.setOnClickListener {
            updateClientInfo()
        }
    }
    fun clickHandler(view: View) {
        when (view.id) {
            R.id.edit_avatar_text -> {
                openFileChooser()
            }
            else -> {

            }
        }
    }

    private fun updateClientInfo() {
        if (validateUserChoice()) {
            Log.d("INSIDE_UPDATE_CLIENT:","Updateing user image")
            val updatedCustomer = Repository.customer
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
                            // Repare Updated user for update
                            updatedCustomer.avatarImageUri = avatarURL
                            updatedCustomer.dateOfBirth = binding.birthDateEditText.text.toString()
                            updatedCustomer.phone = binding.phoneNumberEditText.text.toString()
                            // Find the old user's Document ID
                            db.collection("Customer")
                                .whereEqualTo("email",Repository.customer.email)
                                .limit(1)
                                .get()
                                .addOnSuccessListener {
                                    val userID = it.documents[0].id
                                    db.collection("Customer")
                                        .document(userID)
                                        .set(updatedCustomer)
                                        .addOnSuccessListener {
                                            Log.d("UPDATED_USER:","$updatedCustomer")
                                            Toast.makeText(this, "User Updated succesfully", Toast.LENGTH_SHORT).show()
                                            Repository.customer = updatedCustomer
                                            onBackPressed()
                                        }
                                        .addOnFailureListener{
                                            Toast.makeText(this, "Thay đổi thông tin thất bại, ${it.message}", Toast.LENGTH_SHORT).show()
                                        }
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
        Log.d("INSIDE_UPDATE_CLIENT:","INVALID INFO")

    }

    private fun validateUserChoice(): Boolean {
        val birthDate = binding.birthDateEditText.text.toString()
        val name = binding.nameEditText.text.toString()
        val phone = binding.phoneNumberEditText.text.toString()
        return if (TextUtils.isEmpty(birthDate) || !(isValidDate(birthDate))) {
            Toast.makeText(this, "Please enter a valid birthdate", Toast.LENGTH_SHORT).show()
            Log.d("INSIDE_UPDATE_CLIENT:","RETURNING FALSE from ${binding.birthDateEditText.text.toString()}")
            false
        } else if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Some required fields is missing", Toast.LENGTH_SHORT).show()
            Log.d("INSIDE_UPDATE_CLIENT:","RETURNING FALSE 2 with $name and $phone")
            return false
        } else {
            Log.d("INSIDE_UPDATE_CLIENT:","RETURNING TRUE")
            true
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun isValidDate(value: String?): Boolean {
        return try {
            SimpleDateFormat("dd/mm/yyyy").parse(value)
            Log.d("INSIDE_UPDATE_CLIENT:","RETURNING TRUE DATE")
            true
        } catch (e: ParseException) {
            Log.d("INSIDE_UPDATE_CLIENT:","RETURNING FALSE DATE")
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
            binding.editInfoAvatarImage.setImageURI(avatarImageUri)
            Toast.makeText(this, "User pickec an image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFileExtension(uri: Uri): String? {
        val contentResolver = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(contentResolver.getType(uri))
    }

}