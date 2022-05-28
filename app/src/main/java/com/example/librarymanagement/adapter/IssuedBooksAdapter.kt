package com.example.librarymanagement.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.librarymanagement.R
import com.example.librarymanagement.models.Book
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class IssuedBooksAdapter(var context: Context, var bookList: ArrayList<Book>) :
    RecyclerView.Adapter<IssuedBooksAdapter.BooksViewHolder>() {

    inner class BooksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvBookTitle = itemView.findViewById<TextView>(R.id.txt_book_name)
        val tvBookAuthor = itemView.findViewById<TextView>(R.id.txt_book_author)
        val tvIssuedOn = itemView.findViewById<TextView>(R.id.txt_issued_on)
        val tvIssuedTill = itemView.findViewById<TextView>(R.id.txt_issued_till)
        val parentLayout = itemView.findViewById<MaterialCardView>(R.id.parent_layout)
        val btnReturnBook = itemView.findViewById<ImageButton>(R.id.btn_return_book)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_issued_book, parent, false)

        return BooksViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BooksViewHolder, position: Int) {
        val book = bookList[position]
        holder.tvBookTitle.text = book.name
        holder.tvBookAuthor.text = book.author
        val formatter = SimpleDateFormat("dd MM yyyy", Locale.ENGLISH)
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = book.issuedAt
        holder.tvIssuedOn.text = "Issued on " + formatter.format(calendar.time)
        calendar.timeInMillis = book.issuedTill
        holder.tvIssuedTill.text = "Issued till " + formatter.format(calendar.time)

        holder.btnReturnBook.setOnClickListener {
            onReturnClickListener?.let { click ->
                click(book)
            }
        }


        holder.parentLayout.setOnClickListener {
            onBookClickListener?.let { click ->
                click(book)
            }
        }
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    private var onEditClickListener: ((Book) -> Unit)? = null

    fun setOnEditClickListener(listener: (Book) -> Unit) {
        onEditClickListener = listener
    }

    private var onReturnClickListener: ((Book) -> Unit)? = null

    fun setonReturnClickListener(listener: (Book) -> Unit) {
        onReturnClickListener = listener
    }

    private var onBookClickListener: ((Book) -> Unit)? = null

    fun setOnBookClickListener(listener: (Book) -> Unit) {
        onBookClickListener = listener
    }

}