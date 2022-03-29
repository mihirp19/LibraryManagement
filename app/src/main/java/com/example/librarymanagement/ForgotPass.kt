package com.example.librarymanagement

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class ForgotPass : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)
        val bckBtn2 = findViewById<ImageButton>(R.id.bck_btn_2)
        bckBtn2.setOnClickListener{
            Intent(this,LoginActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}