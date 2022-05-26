package com.example.librarymanagement.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.librarymanagement.R
import com.example.librarymanagement.adapter.BooksAdapter
import com.example.librarymanagement.adapter.BooksAdapterStudent
import com.example.librarymanagement.models.Book
import com.example.librarymanagement.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*

class StudentMainFragment : Fragment(R.layout.fragment_student_main) {

    private val bookCollection = FirebaseFirestore.getInstance().collection("books")
    private val userCollection = FirebaseFirestore.getInstance().collection("user")

    private lateinit var adapter: BooksAdapterStudent

    private lateinit var rvAddBooks: RecyclerView

    private var list = arrayListOf<Book>()

    private val myCalendar = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvAddBooks = view.findViewById(R.id.rv_student_main)

        adapter = BooksAdapterStudent(requireContext(), list)

        getUpdatedList()

        adapter.setOnBookClickListener {
            val bundle = Bundle()
            bundle.putString("0", it.bookUid)
            findNavController().navigate(R.id.bookUiFragment, bundle)
        }

    }

    private fun getUpdatedList() {
        var enrNo = ""
        userCollection.document(Firebase.auth.currentUser!!.uid).get().addOnCompleteListener {
            if (it.isSuccessful) {
                enrNo = it.result.toObject(User::class.java)?.enrolmentNumber ?: ""
                bookCollection.whereEqualTo("issuedBy", enrNo.trim()).get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        list = arrayListOf()
                        list =
                            it.result.toObjects(Book::class.java) as ArrayList<Book>
                        rvAddBooks.layoutManager = LinearLayoutManager(context)
                        adapter = BooksAdapterStudent(requireContext(), list)
                        rvAddBooks.adapter = adapter
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

}