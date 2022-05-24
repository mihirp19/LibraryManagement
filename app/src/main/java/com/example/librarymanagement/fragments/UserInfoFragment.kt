package com.example.librarymanagement.fragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.librarymanagement.R
import com.example.librarymanagement.adapter.BooksAdapter
import com.example.librarymanagement.models.Book
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserInfoFragment : Fragment(R.layout.fragment_user_info) {

    private val userCollection = FirebaseFirestore.getInstance().collection("user")
    private val bookCollection = FirebaseFirestore.getInstance().collection("books")

    private lateinit var adapter: BooksAdapter

    private lateinit var rvBooks: RecyclerView

    private var list = arrayListOf<Book>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvNameUserProfile = view.findViewById<TextView>(R.id.tv_name_user_profile)
        val tvEnrolUserProfile = view.findViewById<TextView>(R.id.tv_enrolment_number_user_profile)
        val tvEmailUserProfile = view.findViewById<TextView>(R.id.tv_email_user_profile)
        val ivEditProfile = view.findViewById<AppCompatImageButton>(R.id.iv_edit_profile)
        rvBooks = view.findViewById(R.id.rv_my_books)

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

        userCollection.document(Firebase.auth.currentUser!!.uid).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val user = it.result.toObject(com.example.librarymanagement.models.User::class.java)
                tvNameUserProfile.text = user?.username
                tvEmailUserProfile.text = user?.email
                tvEnrolUserProfile.text = user?.enrolmentNumber
            }
        }

        ivEditProfile.setOnClickListener {

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