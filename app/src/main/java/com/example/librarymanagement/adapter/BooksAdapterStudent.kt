package com.example.librarymanagement.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.librarymanagement.R
import com.example.librarymanagement.models.Book
import com.google.firebase.firestore.FirebaseFirestore

class BooksAdapterStudent(var context: Context, var bookList: ArrayList<Book>) :
    RecyclerView.Adapter<BooksAdapterStudent.BooksViewHolder>() {

    inner class BooksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvBookTitle = itemView.findViewById<TextView>(R.id.tv_book_title_ui)
        val tvBookAuthor = itemView.findViewById<TextView>(R.id.tv_book_author_ui)
        val tvDescription = itemView.findViewById<TextView>(R.id.tv_description_ui)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_book_layout, parent, false)

        return BooksViewHolder(view)
    }

    override fun onBindViewHolder(holder: BooksViewHolder, position: Int) {
        val book = bookList[position]
        holder.tvBookTitle.text = book.name
        holder.tvBookAuthor.text = book.author
        holder.tvDescription.text = book.description

        holder.itemView.setOnClickListener {
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

    private var onDeleteClickListener: ((Book) -> Unit)? = null

    fun setOnDeleteClickListener(listener: (Book) -> Unit) {
        onDeleteClickListener = listener
    }

    private var onBookClickListener: ((Book) -> Unit)? = null

    fun setOnBookClickListener(listener: (Book) -> Unit) {
        onDeleteClickListener = listener
    }

}