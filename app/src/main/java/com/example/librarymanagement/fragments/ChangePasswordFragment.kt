package com.example.librarymanagement.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.librarymanagement.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChangePasswordFragment : Fragment(R.layout.fragment_change_password) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val etEditUserPassword = view.findViewById<TextInputEditText>(R.id.et_edit_user_password)
        val etEditUserNewPassword =
            view.findViewById<TextInputEditText>(R.id.et_edit_user_new_password)

        val btnChangePassword = view.findViewById<AppCompatButton>(R.id.btn_change_password)

        btnChangePassword.setOnClickListener {
            if (etEditUserPassword.text?.trim().toString()
                    .isEmpty() || etEditUserNewPassword.text?.trim().toString().isEmpty()
            ) {
                Toast.makeText(requireActivity(), "The field must not be empty", Toast.LENGTH_SHORT)
                    .show()
            } else {
                try {

                    val user = Firebase.auth.currentUser

                    val credential = EmailAuthProvider
                        .getCredential(user?.email!!, etEditUserPassword.text?.trim().toString())

                    user.reauthenticate(credential).addOnSuccessListener {
                        CoroutineScope(Dispatchers.Main).launch {
                            user.updatePassword(etEditUserNewPassword.text?.trim().toString())
                                .addOnCompleteListener {
                                    Toast.makeText(
                                        requireActivity(),
                                        "Password Updated successfully!!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireActivity(), "${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}