package com.example.librarymanagement.fragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.librarymanagement.R
import com.example.librarymanagement.models.Book
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BookUiFragment : Fragment(R.layout.fragment_book_ui) {

    private val bookCollection = FirebaseFirestore.getInstance().collection("books")

    private var args: String = ""
    private lateinit var book: Book

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.takeIf { it.containsKey("0") }?.apply {
            args = getString("0")!!
            bookCollection.document(args).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    book =
                        it.result.toObject(Book::class.java)!!
                    val tvBookName = view.findViewById<TextView>(R.id.tv_book_title)
                    val tvBookAuthor = view.findViewById<TextView>(R.id.tv_author)
                    val tvBookDescription = view.findViewById<TextView>(R.id.tv_summary)

                    tvBookName.text = book.name
                    tvBookAuthor.text = book.author
                    tvBookDescription.text = book.description
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "${it.exception?.message}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }
}