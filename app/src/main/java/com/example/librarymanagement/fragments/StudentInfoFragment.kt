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
import com.example.librarymanagement.adapter.BooksAdapterStudent
import com.example.librarymanagement.models.Book
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class StudentInfoFragment : Fragment(R.layout.fragment_student_info) {


    private val userCollection = FirebaseFirestore.getInstance().collection("user")
    private val bookCollection = FirebaseFirestore.getInstance().collection("books")

    private lateinit var adapter: BooksAdapterStudent

    private lateinit var rvBooks: RecyclerView

    private var list = arrayListOf<Book>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvNameUserProfile = view.findViewById<TextView>(R.id.tv_name_student_profile)
        val tvEnrolUserProfile =
            view.findViewById<TextView>(R.id.tv_enrolment_number_student_profile)
        val tvEmailUserProfile = view.findViewById<TextView>(R.id.tv_email_user_profile)
        val ivEditProfile = view.findViewById<AppCompatImageButton>(R.id.iv_edit_profile_student)
        rvBooks = view.findViewById(R.id.rv_my_books)

        getUpdatedList()

        adapter.setOnBookClickListener {
            val bundle = Bundle()
            bundle.putString("0", it.bookUid)
            findNavController().navigate(R.id.bookUiFragment, bundle)
        }

        val currentUser = Firebase.auth.currentUser!!
        try {
            userCollection.document(currentUser.uid).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val user =
                        it.result.toObject(com.example.librarymanagement.models.User::class.java)
                    tvNameUserProfile.text = user?.username
                    tvEmailUserProfile.text = user?.email
                    tvEnrolUserProfile.text = user?.enrolmentNumber
                }
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
        }

        ivEditProfile.setOnClickListener {
            findNavController().navigate(R.id.editProfileFragmentStudent)
        }

    }

    private fun getUpdatedList() {
        bookCollection.get().addOnCompleteListener {
            if (it.isSuccessful) {
                list =
                    it.result.toObjects(Book::class.java) as ArrayList<Book>
            } else {
                Toast.makeText(
                    requireActivity(),
                    "${it.exception?.message}",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
        rvBooks.layoutManager = LinearLayoutManager(context)
        adapter = BooksAdapterStudent(requireContext(), list)
        rvBooks.adapter = adapter
    }
}