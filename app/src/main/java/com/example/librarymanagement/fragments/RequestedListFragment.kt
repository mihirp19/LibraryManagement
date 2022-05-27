package com.example.librarymanagement.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.librarymanagement.R
import com.example.librarymanagement.adapter.RequestBooksAdapter
import com.example.librarymanagement.adapter.RequestedBooksAdapter
import com.example.librarymanagement.models.Book
import com.google.firebase.firestore.FirebaseFirestore

class RequestedListFragment : Fragment(R.layout.fragment_requested_list) {

    private val bookCollection = FirebaseFirestore.getInstance().collection("books")

    private lateinit var adapter: RequestedBooksAdapter

    private lateinit var recyclerView: RecyclerView

    private var list = arrayListOf<Book>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = RequestedBooksAdapter(requireContext(), list)
        recyclerView.adapter = adapter

        getUpdatedList()

    }

    private fun getUpdatedList() {
        bookCollection.whereNotEqualTo("status", "issued").get().addOnCompleteListener {
            if (it.isSuccessful) {
                list =
                    it.result.toObjects(Book::class.java) as ArrayList<Book>
                adapter = RequestedBooksAdapter(requireContext(), list)
                recyclerView.adapter = adapter

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