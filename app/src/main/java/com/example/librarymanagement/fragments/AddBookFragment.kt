package com.example.librarymanagement.fragments

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
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
import java.text.SimpleDateFormat
import java.util.*


class AddBookFragment : Fragment(R.layout.fragment_add_book) {

    private val bookCollection = FirebaseFirestore.getInstance().collection("books")

    private lateinit var adapter: BooksAdapter

    private lateinit var rvAddBooks: RecyclerView

    private var list = arrayListOf<Book>()

    private val myCalendar = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etBookName = view.findViewById<EditText>(R.id.et_name_of_book)
        val etBookAuthor = view.findViewById<EditText>(R.id.et_author_of_book)
        val etBookDescription = view.findViewById<EditText>(R.id.et_description_of_book)
        val etPublishYear = view.findViewById<EditText>(R.id.et_year)
        val etBookCategory = view.findViewById<EditText>(R.id.et_category_of_book)
        val etIssuedBy = view.findViewById<EditText>(R.id.et_issued_by)
        val selectDate = view.findViewById<EditText>(R.id.select_data)
        val btnAddBook = view.findViewById<AppCompatButton>(R.id.btn_add_book)
        rvAddBooks = view.findViewById(R.id.rv_books_add_book_frag)

        val date =
            OnDateSetListener { _, year, month, day ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, day)
                val myFormat = "MM/dd/yy"
                val dateFormat = SimpleDateFormat(myFormat, Locale.US)
                selectDate.setText(dateFormat.format(myCalendar.time))
            }

        selectDate.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                date,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()

        }

        getUpdatedList()

        adapter.setOnDeleteClickListener { book ->
            bookCollection.document(book.bookUid).delete().addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(requireActivity(), "Book deleted!", Toast.LENGTH_SHORT).show()
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

        adapter.setOnBookClickListener {
            val bundle = Bundle()
            bundle.putString("0", it.bookUid)
            findNavController().navigate(R.id.bookUiFragment, bundle)
        }

        btnAddBook.setOnClickListener {

            if (etBookName.text.trim().toString().isEmpty() || etBookAuthor.text.trim().toString()
                    .isEmpty() || etPublishYear.text.trim().toString()
                    .isEmpty() || etBookCategory.text.trim().toString()
                    .isEmpty() || etBookDescription.text.trim().toString()
                    .isEmpty() || etIssuedBy.text.trim().toString()
                    .isEmpty() || selectDate.text.toString().isEmpty()
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
                    issuedBy = etIssuedBy.text.trim().toString(),
                    description = etBookDescription.text.trim().toString(),
                    issuedFrom = curUser.uid,
                    issuedAt = System.currentTimeMillis(),
                    issuedTill = myCalendar.timeInMillis
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
                            etIssuedBy.setText("")
                            selectDate.setText("")
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