package com.example.librarymanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        val bckBtn = findViewById<ImageButton>(R.id.bck_btn)
        val forBtn = findViewById<TextView>(R.id.for_pass_tv)
        val loginBtn = findViewById<Button>(R.id.login_btn2)
        bckBtn.setOnClickListener {

        }

        loginBtn.setOnClickListener {
            val emailEt = findViewById<EditText>(R.id.email_tv)
            val passwordEt = findViewById<EditText>(R.id.pass_tv)

            if (emailEt.text.isNotEmpty() && passwordEt.text.isNotEmpty()){
                mAuth.signInWithEmailAndPassword(emailEt.text.toString(), passwordEt.text.toString()).addOnCompleteListener { task->
                    if(task.isSuccessful){
                        Toast.makeText(this, "Successfully logged in!", Toast.LENGTH_LONG).show()
                        Intent(this, HomeActivity::class.java).also {
                            startActivity(it)
                        }
                    }else{
                        Log.d("LoginActivity: ", task.exception.toString())
                    }
                }
            }else{
                Toast.makeText(this, "Required field cannot be empty", Toast.LENGTH_LONG).show()
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
