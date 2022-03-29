package com.example.librarymanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView

class SignIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        val bckBtn = findViewById<ImageButton>(R.id.bck_btn)
        val forBtn = findViewById<TextView>(R.id.tv_reg)

        bckBtn.setOnClickListener{
            onBackPressed()
        }
        forBtn.setOnClickListener{
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