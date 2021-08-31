package com.example.centergaragemanagementsystem.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.centergaragemanagementsystem.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import login.LoginActivity

// TODO: Add EditText + DateTimePicker for dateOfBirth
//  Add Image picker for choosing user Avatar: Store the Image in cloud firestore and store the URI in avatarURI field
//  May need to check for additional validity: Account Existed.


class RegisterActivity : AppCompatActivity() {
    private lateinit var emailOrUserNameView : EditText
    private lateinit var passwordView: EditText
    private lateinit var passwordConfirmView: EditText
    private lateinit var showHidePassView : CheckBox
    private lateinit var phoneView: EditText
    private lateinit var nameView: EditText
    private lateinit var continueButton : Button
    private lateinit var progressBarView : ProgressBar
    private lateinit var auth: FirebaseAuth;
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        initializeUI();

        continueButton.setOnClickListener{
            registerNewUser()
        }
    }

    private fun registerNewUser(){
        progressBarView.visibility = View.VISIBLE
        if(validateUserChoice()){
            val email = emailOrUserNameView.text.toString()
            val password = passwordConfirmView.text.toString()
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        val user = hashMapOf(
                            "dateOfBirth" to "12/12/2000",
                            "avatarURI" to "null",
                            "name" to nameView.text.toString(),
                            "email" to emailOrUserNameView.text.toString(),
                            "phone" to phoneView.text.toString(),
                            "role" to "manager"
                        )
                        db.collection("User")
                            .add(user)
                            .addOnSuccessListener {
                                Log.d("REGISTER:","New account added to firestore")
                                loadLoginScreen()
                                progressBarView.visibility = View.GONE
                                Toast.makeText(this, "registration successful",
                                    Toast.LENGTH_SHORT).show()

                            }
                            .addOnFailureListener{
                                Log.d("REGISTER:", "FAILED TO ADD ACCOUNT TO DATABASE")
                                Toast.makeText(this, "regis OK. Failed to load to FireStore", Toast.LENGTH_SHORT).show()

                            }
                    }
                    else{
                        Toast.makeText(
                            this,
                            "Something went wrong. Failed to register",
                            Toast.LENGTH_SHORT
                        ).show()
                        progressBarView.visibility = View.GONE
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }
        }

    }

    private fun loadLoginScreen(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun loadHomeScreenPage(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }


    private fun initializeUI(){
        emailOrUserNameView = findViewById(R.id.register_email_edit_text)
        nameView = findViewById(R.id.register_name_edit_text)
        phoneView = findViewById(R.id.register_phone_num_edit_text)
        passwordView = findViewById(R.id.register_password_edit_text)
        passwordConfirmView = findViewById(R.id.register_password_confirm_edit_text)
        continueButton = findViewById(R.id.register_confirm_button)
        progressBarView = findViewById(R.id.register_progress_bar)
    }
    private fun validateUserChoice() : Boolean{
        val email = emailOrUserNameView.text.toString()
        val password = passwordConfirmView.text.toString()
        if (passwordView.text.toString() != passwordConfirmView.text.toString()){
            Toast.makeText(this, "Mismatch in password and confirmation", Toast.LENGTH_SHORT).show()
            return false
        }
        else if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(this, "Some required fields is missing", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

}