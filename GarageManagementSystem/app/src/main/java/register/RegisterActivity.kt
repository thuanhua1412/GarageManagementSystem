package register

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.dataclasses.Customer
import com.example.dataclasses.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import customer.homepage.HomeScreenActivity
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
        if(validateUserChoice()){
            val userName = nameView.text.toString()
            val phoneNum = phoneView.text.toString()
            val emailText = emailOrUserNameView.text.toString()
            val customer  = Customer(name = userName,dateOfBirth = "",avatarImageUri = "",phone = phoneNum
            , role = "customer",email = emailText)

            navigateToRegisterEnd(customer)
        }

    }
    private fun navigateToRegisterEnd(customer: Customer){
        val bundle = Bundle()
        bundle.putParcelable("customer", customer)
        bundle.putString("password",passwordConfirmView.text.toString())
        val intent = Intent(this, RegisterEndActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    private fun loadLoginScreen(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun loadHomeScreenPage(){
        val intent = Intent(this, HomeScreenActivity::class.java)
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