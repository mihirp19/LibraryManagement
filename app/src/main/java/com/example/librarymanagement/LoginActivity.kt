package com.example.librarymanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val bckBtn = findViewById<ImageButton>(R.id.bck_btn)
        val forBtn = findViewById<TextView>(R.id.for_pass_tv)
        val loginBtn = findViewById<Button>(R.id.login_btn2)
        bckBtn.setOnClickListener {
            onBackPressed()
        }

        loginBtn.setOnClickListener {
            Intent(this, HomeActivity::class.java).also {
                startActivity(it)
            }
        }

        forBtn.setOnClickListener{
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
