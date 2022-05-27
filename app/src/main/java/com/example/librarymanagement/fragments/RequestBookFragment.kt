package com.example.librarymanagement.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.librarymanagement.R
import com.example.librarymanagement.adapter.BooksAdapter
import com.example.librarymanagement.adapter.RequestBooksAdapter
import com.example.librarymanagement.models.Book
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*

class RequestBookFragment : Fragment(R.layout.fragment_request_book) {

    private val bookCollection = FirebaseFirestore.getInstance().collection("books")

    private lateinit var adapter: RequestBooksAdapter

    private lateinit var recyclerView: RecyclerView

    private var list = arrayListOf<Book>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rv_request_book)

        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = RequestBooksAdapter(requireContext(), list)
        recyclerView.adapter = adapter

        getUpdatedList()

    }

    private fun getUpdatedList() {
        bookCollection.whereNotEqualTo("status", "issued").get().addOnCompleteListener {
            if (it.isSuccessful) {
                list =
                    it.result.toObjects(Book::class.java) as ArrayList<Book>
                adapter = RequestBooksAdapter(requireContext(), list)
                recyclerView.adapter = adapter

                adapter.setonRequestClickListener {
                    bookCollection.document(it.bookUid)
                        .update(
                            "requestedBy",
                            FieldValue.arrayUnion(Firebase.auth.currentUser!!.uid),
                            "status",
                            "requested"
                        ).addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(requireActivity(), "Requested", Toast.LENGTH_SHORT)
                                    .show()
                                getUpdatedList()
                            } else {
                                Toast.makeText(
                                    requireActivity(),
                                    it.exception?.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }

                adapter.setOnBookClickListener {
                    val bundle = Bundle()
                    bundle.putString("0", it.bookUid)
                    findNavController().navigate(R.id.bookUiFragment2, bundle)
                }

            } else {
                Toast.makeText(requireActivity(), "${it.exception?.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

}