package com.example.librarymanagement.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.librarymanagement.R
import com.example.librarymanagement.adapter.RequestedUsersAdapter
import com.example.librarymanagement.models.Book
import com.example.librarymanagement.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class RequestedUsersFragment : Fragment(R.layout.fragment_requested_users) {

    private val bookCollection = FirebaseFirestore.getInstance().collection("books")
    private val usersCollection = FirebaseFirestore.getInstance().collection("user")

    private lateinit var adapter: RequestedUsersAdapter

    private lateinit var recyclerView: RecyclerView

    private var args: String = ""
    private lateinit var book: Book

    private var list = arrayListOf<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = RequestedUsersAdapter(requireContext(), list)
        recyclerView.adapter = adapter

        arguments?.takeIf { it.containsKey("0") }?.apply {
            args = getString("0")!!
            bookCollection.document(args).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    book =
                        it.result.toObject(Book::class.java)!!
                    getUpdatedList()
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

    private fun getUpdatedList() {
        bookCollection.whereEqualTo("status", "requested").get().addOnCompleteListener {
            if (it.isSuccessful) {
                adapter = RequestedUsersAdapter(requireContext(), book.requestedBy)
                recyclerView.adapter = adapter

                adapter.setOnUserClickListener {
                    bookCollection.document(book.bookUid)
                        .update(
                            "status",
                            "issued",
                            "issuedBy",
                            Firebase.auth.currentUser!!.uid,
                            "issuedAt",
                            System.currentTimeMillis()
                        )
                    findNavController().navigate(R.id.requestedListFragment)
                }

            } else {
                Toast.makeText(requireActivity(), "${it.exception?.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

}