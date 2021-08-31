package login

import repository.Repository
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.dataclasses.Customer
import com.example.dataclasses.R
import com.example.dataclasses.Service
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import customer.homepage.HomeScreenActivity
import register.RegisterActivity

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class LoginActivity : AppCompatActivity() {
    private lateinit var emailUserNameView: EditText
    private lateinit var passwordView: EditText
    private lateinit var loginButton: Button
    private lateinit var registerText: TextView
    private lateinit var loginText: TextView
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initializeUI()
        loginButton.setOnClickListener {
            authorizeAndLoginUserAccount()
        }
        auth = FirebaseAuth.getInstance()

        if(intent.extras != null){
            val bundle  = intent.extras!!
            emailUserNameView.setText(bundle.getString("username"))
            passwordView.setText(bundle.getString("password"))
        }
    }

    private fun authorizeAndLoginUserAccount() {
        val email = emailUserNameView.text.toString()
        val password = passwordView.text.toString()
        if (validateUserChoice()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        db.collection("Customer")
                            .whereEqualTo("email", email)
                            .limit(1)
                            .get()
                            .addOnSuccessListener {
                                if (it.documents[0].data?.get("role") == "customer") {
                                    Toast.makeText(this, "Welcome back, $email", Toast.LENGTH_SHORT)
                                        .show()
                                    val customer = it.documents[0].toObject(Customer::class.java)
                                    Repository.customer = customer!!
                                    Log.d("LOGIN_CUSTOMER:","Customer: ${Repository.customer}")
                                    loadHomeScreenPage(auth.currentUser)
                                } else {
                                    Toast.makeText(
                                        this,
                                        "This app is for customer only",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    false
                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    this,
                                    "Network failure. Cant Authorize",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }

        }

    }
    private fun validateUserChoice(): Boolean {
        val email = emailUserNameView.text.toString()
        val password = passwordView.text.toString()
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Ughh. Someone forgot to input email", Toast.LENGTH_SHORT).show()
            return false
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Ughh. Password maybe empty", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun initializeUI() {
        emailUserNameView = findViewById(R.id.user_account_edit_text)
        passwordView = findViewById(R.id.user_password_edit_text)
        loginButton = findViewById(R.id.login_button)
        registerText = findViewById(R.id.register_text)
        loginText = findViewById(R.id.login_text)

    }

    private fun loadRegisterPage() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun loadHomeScreenPage(user: FirebaseUser) {
        val intent = Intent(this, HomeScreenActivity::class.java)
        startActivity(intent)
    }

    companion object {
        private const val RC_SIGN_IN = 123
    }

    fun onTextViewClick(view: View) {
        when (view.id) {
            R.id.register_text -> loadRegisterPage()
            R.id.login_text -> authorizeAndLoginUserAccount()
        }
    }

    private fun updateFirebaseField(){
//        db.collection("Customer")
//            .get()
//            .addOnSuccessListener {
//                var serviceList : List<Customer> = it.toObjects(Customer::class.java)
//                Log.d("LOGIN_LOG:","List of service gotten is: $serviceList")
//
//            }
    }
}


// TODO: Code Grave Yard trying to integrate google sign-in into the app (Didn't work)
//@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
//class LoginActivity : AppCompatActivity() {
//    private var clientOathID = "683078241722-v93206rmhtk8slrviofg7kpq4bm06g9f.apps.googleusercontent.com"
//    private lateinit var googleSignInClient : GoogleSignInClient;
//    private lateinit var user : FirebaseUser
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
////        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
////            .requestIdToken(clientOathID)
////            .requestEmail()
////            .build()
////        googleSignInClient = GoogleSignIn.getClient(this,gso)
////        val googleSignInButton  = findViewById<Button>(R.id.google_sign_in_button)
//
//        val loginButton = findViewById<Button>(R.id.login_button)
//        loginButton.setOnClickListener{
//            if(user == null) {
//                Toast.makeText(this,"Failed to Authenticate user",Toast.LENGTH_SHORT).show()
//            }
//            else loadHomeScreenPage(user)
//        }
//
//        val registerButton = findViewById<TextView>(R.id.register_text)
//        registerButton.setOnClickListener{
//            loadRegisterPage()
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode == RC_SIGN_IN){
//            val response = IdpResponse.fromResultIntent(data)
//            if(resultCode == Activity.RESULT_OK){ //Sign In Successful
//                user = FirebaseAuth.getInstance().currentUser
//                // TODO: Load the homescreen page and pass the userID to the
//                //  activity
//                loadHomeScreenPage(user)
//            }
//            else{
//                Toast.makeText(this,"Failed to sign in",Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    override fun onStart() {
//        super.onStart()
//        val currentUser = FirebaseAuth.getInstance().currentUser;
////        if(currentUser != null)
//    }
//
//    private fun createSignInIntent(){
//        val providers = arrayListOf(
//            AuthUI.IdpConfig.EmailBuilder().build(),
//            AuthUI.IdpConfig.GoogleBuilder().build(),
//            AuthUI.IdpConfig.FacebookBuilder().build()
//        )
//
//        startActivityForResult(
//            AuthUI.getInstance()
//                .createSignInIntentBuilder()
//                .setAvailableProviders(providers)
//                .build(),
//            RC_SIGN_IN
//        )
//    }
//
//    private fun signOut(){
//        AuthUI.getInstance()
//            .signOut(this)
//            .addOnCompleteListener{
//                Toast.makeText(this,"Signed out",Toast.LENGTH_SHORT).show()
//            }
//    }
//
//    private fun delete(){
//        AuthUI.getInstance()
//            .delete(this)
//            .addOnCompleteListener{
//
//            }
//    }
//
//    private fun loadRegisterPage(){
//        val intent = Intent(this, RegisterActivity::class.java)
//        startActivity(intent)
//    }
//    private fun loadHomeScreenPage(user: FirebaseUser){
//        val intent = Intent(this, HomeScreenActivity::class.java)
//        startActivity(intent)
//    }
//    companion object {
//        private const val RC_SIGN_IN = 123
//    }
//}