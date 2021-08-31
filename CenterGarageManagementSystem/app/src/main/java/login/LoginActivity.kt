package login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.centergaragemanagementsystem.R
import com.example.centergaragemanagementsystem.activity.HomeActivity
import com.example.centergaragemanagementsystem.activity.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class LoginActivity : AppCompatActivity() {
    private lateinit var emailUserNameView: EditText
    private lateinit var passwordView: EditText
    private lateinit var loginButton: Button
    private lateinit var registerText: TextView
    private lateinit var loginText: TextView
    private lateinit var progressBarView: ProgressBar
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
    }

    private fun authorizeAndLoginUserAccount() {
        progressBarView.isVisible = false
        val email = emailUserNameView.text.toString()
        val password = passwordView.text.toString()
        if (validateUserChoice()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        db.collection("User")
                            .whereEqualTo("email", email)
                            .limit(1)
                            .get()
                            .addOnSuccessListener {
                                if (it.documents[0].data?.get("role") == "manager") {
                                    Log.d("LOGIN:", "${it.documents[0].data?.get("role")}")
                                    Toast.makeText(this, "Welcome back, $email", Toast.LENGTH_SHORT)
                                        .show()
                                    loadHomeScreenPage(auth.currentUser)
                                } else {
                                    Toast.makeText(
                                        this,
                                        "This app is for manager only",
                                        Toast.LENGTH_SHORT
                                    ).show()
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
        progressBarView = findViewById(R.id.login_progress_bar)

    }

    private fun loadRegisterPage() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun loadHomeScreenPage(user: FirebaseUser?) {
        val intent = Intent(this, HomeActivity::class.java)
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

}