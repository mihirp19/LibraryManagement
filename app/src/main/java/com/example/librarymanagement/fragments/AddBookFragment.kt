package com.example.librarymanagement.fragments

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.librarymanagement.R
import com.example.librarymanagement.adapter.BooksAdapter
import com.example.librarymanagement.models.Book
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class AddBookFragment : Fragment(R.layout.fragment_add_book) {

    private val bookCollection = FirebaseFirestore.getInstance().collection("books")

    private lateinit var adapter: BooksAdapter

    private lateinit var rvAddBooks: RecyclerView

    private var list = arrayListOf<Book>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etBookName = view.findViewById<EditText>(R.id.et_name_of_book)
        val etBookAuthor = view.findViewById<EditText>(R.id.et_author_of_book)
        val etPublishYear = view.findViewById<EditText>(R.id.et_year)
        val etBookCategory = view.findViewById<EditText>(R.id.et_category_of_book)
        val btnAddBook = view.findViewById<AppCompatButton>(R.id.btn_add_book)
        rvAddBooks = view.findViewById(R.id.rv_books_add_book_frag)

        getUpdatedList()

        adapter.setOnDeleteClickListener { book ->
            bookCollection.document(book.bookUid).delete().addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(requireActivity(), "Book deleted!", Toast.LENGTH_SHORT).show()
                    getUpdatedList()
                } else {
                    Toast.makeText(requireActivity(), "${it.exception?.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        adapter.setOnBookClickListener {
            val bundle = Bundle()
            bundle.putString("0", it.bookUid)
            findNavController().navigate(R.id.bookUiFragment, bundle)
        }

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
                            getUpdatedList()
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

    private fun getUpdatedList() {
        bookCollection.get().addOnCompleteListener {
            if (it.isSuccessful) {
                list =
                    it.result.toObjects(Book::class.java) as ArrayList<Book>
            } else {
                Toast.makeText(requireActivity(), "${it.exception?.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        rvAddBooks.layoutManager = LinearLayoutManager(context)
        adapter = BooksAdapter(requireContext(), list)
        rvAddBooks.adapter = adapter
    }

}