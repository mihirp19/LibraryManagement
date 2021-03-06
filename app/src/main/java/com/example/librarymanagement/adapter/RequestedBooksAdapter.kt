package com.example.librarymanagement.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.librarymanagement.R
import com.example.librarymanagement.models.Book
import com.google.android.material.card.MaterialCardView

class RequestedBooksAdapter(var context: Context, var bookList: ArrayList<Book>) :
    RecyclerView.Adapter<RequestedBooksAdapter.BooksViewHolder>() {

    inner class BooksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvBookTitle = itemView.findViewById<TextView>(R.id.txt_book_name)
        val tvBookAuthor = itemView.findViewById<TextView>(R.id.txt_book_author)
        val parentLayout = itemView.findViewById<MaterialCardView>(R.id.parent_layout)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)

        return BooksViewHolder(view)
    }

    override fun onBindViewHolder(holder: BooksViewHolder, position: Int) {
        val book = bookList[position]
        holder.tvBookTitle.text = book.name
        holder.tvBookAuthor.text = book.author


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

    private var onRequestClickListener: ((Book) -> Unit)? = null

    fun setonRequestClickListener(listener: (Book) -> Unit) {
        onRequestClickListener = listener
    }

    private var onBookClickListener: ((Book) -> Unit)? = null

    fun setOnBookClickListener(listener: (Book) -> Unit) {
        onBookClickListener = listener
    }

}