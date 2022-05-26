package com.example.librarymanagement.fragments

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.librarymanagement.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etUsername = view.findViewById<EditText>(R.id.et_ed_username)
        val btnChangeUsername = view.findViewById<AppCompatButton>(R.id.apc_change_username)
        val btnChangePass = view.findViewById<AppCompatButton>(R.id.apc_change_pass)

        btnChangeUsername.setOnClickListener {
            if (etUsername.text.trim().toString().isNotEmpty()) {
                val auth = FirebaseAuth.getInstance()
                val curUser = auth.currentUser
                val profileUpdate =
                    UserProfileChangeRequest.Builder()
                        .setDisplayName(etUsername.text.trim().toString()).build()

                curUser!!.updateProfile(profileUpdate).addOnCompleteListener {
                    Toast.makeText(
                        requireContext(),
                        "Profile Updated successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        btnChangePass.setOnClickListener {
            findNavController().navigate(R.id.changePasswordFragment)
        }
    }
}