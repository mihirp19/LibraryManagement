package com.example.librarymanagement.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.librarymanagement.LoginActivity
import com.example.librarymanagement.MainActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignOutDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Sign out?")
            .setMessage("Do you want to sign out from app?")
            .setPositiveButton("Yes", null)
            .create()

        dialog.setOnShowListener {
            val button = dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {

                    Firebase.auth.signOut()

                    Intent(requireContext(), MainActivity::class.java).also { intent ->
                        startActivity(intent)
                        requireActivity().finish()

                    }
                }
            }
        }
        return dialog

    }

}