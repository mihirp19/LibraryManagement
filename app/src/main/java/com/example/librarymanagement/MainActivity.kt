package com.example.librarymanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        if(currentUser?.uid != null){
            Intent(this, HomeActivity::class.java).also { intent ->
                startActivity(intent)
                finish()
            }
        }

        val loginBtn = findViewById<Button>(R.id.login_btn)
        val signinBtn = findViewById<Button>(R.id.signin_btn)
        loginBtn.setOnClickListener {
            Intent(this, LoginActivity::class.java).also {
                startActivity(it)
            }
        }

        signinBtn.setOnClickListener{
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