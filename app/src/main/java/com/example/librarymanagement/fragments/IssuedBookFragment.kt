package com.example.librarymanagement.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.librarymanagement.R
import com.example.librarymanagement.adapter.IssuedBooksAdapter
import com.example.librarymanagement.adapter.RequestedBooksAdapter
import com.example.librarymanagement.models.Book
import com.google.firebase.firestore.FirebaseFirestore

class IssuedBookFragment : Fragment(R.layout.fragment_issued_books) {

    private val bookCollection = FirebaseFirestore.getInstance().collection("books")

    private lateinit var adapter: IssuedBooksAdapter

    private lateinit var recyclerView: RecyclerView

    private var list = arrayListOf<Book>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = IssuedBooksAdapter(requireContext(), list)
        recyclerView.adapter = adapter

        getUpdatedList()

    }

    private fun getUpdatedList() {
        bookCollection.whereEqualTo("status", "issued").get().addOnCompleteListener {
            if (it.isSuccessful) {
                list =
                    it.result.toObjects(Book::class.java) as ArrayList<Book>
                adapter = IssuedBooksAdapter(requireContext(), list)
                recyclerView.adapter = adapter

                adapter.setonReturnClickListener {
                    bookCollection.document(it.bookUid).update(
                        "status",
                        "",
                        "issuedBy",
                        "",
                        "issuedAt",
                        0L,
                        "issuedTill",
                        0L
                    ).addOnCompleteListener {
                        getUpdatedList()
                    }
                }

                adapter.setOnBookClickListener {
                    val bundle = Bundle()
                    bundle.putString("0", it.bookUid)
                    findNavController().navigate(R.id.requestedUsersFragment, bundle)
                }

            } else {
                Toast.makeText(requireActivity(), "${it.exception?.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

}