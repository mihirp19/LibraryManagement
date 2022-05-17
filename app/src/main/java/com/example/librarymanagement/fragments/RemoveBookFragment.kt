package com.example.librarymanagement.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.librarymanagement.R
import com.example.librarymanagement.adapter.BooksAdapter
import com.example.librarymanagement.models.Book
import com.google.firebase.firestore.FirebaseFirestore

class RemoveBookFragment : Fragment(R.layout.fragment_remove_book) {

    private val bookCollection = FirebaseFirestore.getInstance().collection("books")

    private lateinit var adapter: BooksAdapter

    private lateinit var rvBooks: RecyclerView

    private var list = arrayListOf<Book>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvBooks = view.findViewById(R.id.rv_remove_book_frag)

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
        rvBooks.layoutManager = LinearLayoutManager(context)
        adapter = BooksAdapter(requireContext(), list)
        rvBooks.adapter = adapter
    }
}