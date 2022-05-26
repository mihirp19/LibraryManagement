package com.example.librarymanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.librarymanagement.models.User
import com.google.firebase.FirebaseError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class SignIn : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private val userCollection = FirebaseFirestore.getInstance().collection("user")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        mAuth = FirebaseAuth.getInstance()

        val bckBtn = findViewById<ImageButton>(R.id.bck_btn)
        val forBtn = findViewById<TextView>(R.id.tv_reg)
        val sigupBtn = findViewById<Button>(R.id.signup_btn)
        val passwordTv = findViewById<EditText>(R.id.et_pass)
        val nameTv = findViewById<EditText>(R.id.et_username)
        val emailTv = findViewById<EditText>(R.id.et_email)
        val enoTv = findViewById<EditText>(R.id.et_erno)


        sigupBtn.setOnClickListener {
            if (passwordTv.text.isEmpty() || nameTv.text.isEmpty() || emailTv.text.isEmpty() || enoTv.text.isEmpty()) {
                Toast.makeText(this, "Required Fields can't be empty", Toast.LENGTH_LONG).show()
            } else {
                var role = "student"
                if (enoTv.text.trim().toString()[0] == 'A') {
                    role = "admin"
                }
                mAuth.createUserWithEmailAndPassword(
                    emailTv.text.toString(),
                    passwordTv.text.toString()
                ).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@SignIn, "Signup Successful", Toast.LENGTH_LONG).show()
                        userCollection.document(task.result.user!!.uid).set(
                            User(
                                username = nameTv.text.trim().toString(),
                                enrolmentNumber = enoTv.text.trim().toString(),
                                email = emailTv.text.trim().toString(),
                                uid = task.result.user!!.uid,
                                role = role
                            )
                        )
                        Intent(this, LoginActivity::class.java).also { intent ->
                            startActivity(intent)
                            this.finish()
                        }
                    } else {
                        Log.d("SignInActivity: ", task.exception.toString())
                    }
                }
            }
        }

        bckBtn.setOnClickListener {
            onBackPressed()
        }
        forBtn.setOnClickListener {
            Intent(this, LoginActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Intent(this, MainActivity::class.java).also {
            startActivity(it)
        }
    }
}