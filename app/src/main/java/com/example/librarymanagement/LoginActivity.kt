package com.example.librarymanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.librarymanagement.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private val userCollection = FirebaseFirestore.getInstance().collection("user")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        val bckBtn = findViewById<ImageButton>(R.id.bck_btn)
        val forBtn = findViewById<TextView>(R.id.for_pass_tv)
        val loginBtn = findViewById<Button>(R.id.login_btn2)
        bckBtn.setOnClickListener {
            onBackPressed()
        }

        loginBtn.setOnClickListener {
            val emailEt = findViewById<EditText>(R.id.email_tv)
            val passwordEt = findViewById<EditText>(R.id.pass_tv)

            if (emailEt.text.isNotEmpty() && passwordEt.text.isNotEmpty()) {
                mAuth.signInWithEmailAndPassword(
                    emailEt.text.trim().toString(),
                    passwordEt.text.trim().toString()
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Successfully logged in!", Toast.LENGTH_LONG).show()
                        val currentUser = task.result.user
                        if (currentUser?.uid != null) {
                            try {
                                userCollection.document(currentUser.uid).get()
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            val user = it.result.toObject(User::class.java)
                                            if (user?.role?.trim() == "admin") {
                                                Intent(
                                                    this,
                                                    AdminActivity::class.java
                                                ).also { intent ->
                                                    startActivity(intent)
                                                    this.finish()
                                                }
                                            } else {
                                                Intent(
                                                    this,
                                                    StudentActivity::class.java
                                                ).also { intent ->
                                                    startActivity(intent)
                                                    this.finish()
                                                }
                                            }
                                        }
                                    }
                            } catch (e: Exception) {
                                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Log.d("LoginActivity: ", task.exception.toString())
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Required field cannot be empty", Toast.LENGTH_LONG).show()
            }
        }

        forBtn.setOnClickListener {
            Intent(this, ForgotPass::class.java).also {
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
