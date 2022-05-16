package com.example.librarymanagement.fragments

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.librarymanagement.R
import com.example.librarymanagement.models.Book
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class AddBookFragment : Fragment(R.layout.fragment_add_book) {

    private val bookCollection = FirebaseFirestore.getInstance().collection("books")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etBookName = view.findViewById<EditText>(R.id.et_name_of_book)
        val etBookAuthor = view.findViewById<EditText>(R.id.et_author_of_book)
        val etPublishYear = view.findViewById<EditText>(R.id.et_year)
        val etBookCategory = view.findViewById<EditText>(R.id.et_category_of_book)
        val btnAddBook = view.findViewById<AppCompatButton>(R.id.btn_add_book)

        btnAddBook.setOnClickListener {

            if (etBookName.text.trim().toString().isEmpty() || etBookAuthor.text.trim().toString()
                    .isEmpty() || etPublishYear.text.trim().toString()
                    .isEmpty() || etBookCategory.text.trim().toString().isEmpty()
            ) {
                Toast.makeText(requireActivity(), "Enter all fields", Toast.LENGTH_SHORT).show()
            } else {
                val uid = UUID.randomUUID().toString().replace("-", "")
                val curUser = Firebase.auth.currentUser!!

                val book = Book(
                    bookUid = uid,
                    name = etBookName.text.trim().toString(),
                    author = etBookAuthor.text.trim().toString(),
                    year = etPublishYear.text.trim().toString(),
                    category = etBookCategory.text.trim().toString(),
                    issuedBy = curUser.uid
                )

                CoroutineScope(Dispatchers.Main).launch {
                    bookCollection.document(uid).set(book).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(requireActivity(), "Book added", Toast.LENGTH_SHORT)
                                .show()
                            etBookName.setText("")
                            etPublishYear.setText("")
                            etBookAuthor.setText("")
                            etBookCategory.setText("")
                        } else {
                            Toast.makeText(
                                requireActivity(),
                                "${it.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }
}