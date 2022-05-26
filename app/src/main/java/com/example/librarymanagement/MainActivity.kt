package com.example.librarymanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.librarymanagement.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private val userCollection = FirebaseFirestore.getInstance().collection("user")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        if (currentUser?.uid != null) {
            try {
                userCollection.document(currentUser.uid).get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        val user = it.result.toObject(User::class.java)
                        if (user?.role?.trim() == "admin") {
                            Intent(this, AdminActivity::class.java).also { intent ->
                                startActivity(intent)
                                this.finish()
                            }
                        } else {
                            Intent(this, StudentActivity::class.java).also { intent ->
                                startActivity(intent)
                                this.finish()
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }

        val loginBtn = findViewById<Button>(R.id.login_btn)
        val signinBtn = findViewById<Button>(R.id.signin_btn)
        loginBtn.setOnClickListener {
            Intent(this, LoginActivity::class.java).also {
                startActivity(it)
            }
        }

        signinBtn.setOnClickListener {
            Intent(this, SignIn::class.java).also {
                startActivity(it)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()

    }
}