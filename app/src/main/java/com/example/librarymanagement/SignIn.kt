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
    private lateinit var mAuth:FirebaseAuth
    private val userCollection = FirebaseFirestore.getInstance().collection("user")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        mAuth = FirebaseAuth.getInstance()

        val bckBtn = findViewById<ImageButton>(R.id.bck_btn)
        val forBtn = findViewById<TextView>(R.id.tv_reg)
        val sigupBtn = findViewById<Button>(R.id.signup_btn)

        sigupBtn.setOnClickListener {
            val user = checkFields()
            if(user != null) {
                val passwordTv = findViewById<EditText>(R.id.et_pass)
                mAuth.createUserWithEmailAndPassword(user.email, passwordTv.text.toString()).addOnCompleteListener(
                    this
                ) { task ->
                    if(task.isSuccessful){
                        Toast.makeText(this@SignIn, "Signup Successful", Toast.LENGTH_LONG).show()
                        userCollection.document(user.uid).set(user)
                    } else {
                        Log.d("SignInActivity: ",task.exception.toString())
                    }
                }
            }
        }

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

    private fun checkFields(): User? {
        var user: User? = null
        val passwordTv = findViewById<EditText>(R.id.et_pass)
        val nameTv = findViewById<EditText>(R.id.et_username)
        val emailTv = findViewById<EditText>(R.id.et_email)
        val enoTv = findViewById<EditText>(R.id.et_erno)

        val uid = UUID.randomUUID().toString().replace("-", "")

        if(passwordTv.text.isEmpty() || nameTv.text.isEmpty() || emailTv.text.isEmpty() || enoTv.text.isEmpty()) {
            Toast.makeText(this, "Required Fields can't be empty", Toast.LENGTH_LONG).show()
        } else {
            user = User(
                username = nameTv.text.toString(),
                enrolmentNumber = enoTv.text.toString(),
                email = emailTv.text.toString(),
                uid = uid
            )
        }

        return user
    }
}