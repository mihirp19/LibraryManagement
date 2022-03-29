package com.example.librarymanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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