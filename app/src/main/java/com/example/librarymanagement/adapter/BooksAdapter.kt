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

class BooksAdapter(var context: Context, var bookList: ArrayList<Book>) :
    RecyclerView.Adapter<BooksAdapter.BooksViewHolder>() {

    inner class BooksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvBookTitle = itemView.findViewById<TextView>(R.id.txt_book_name)
        val tvBookAuthor = itemView.findViewById<TextView>(R.id.txt_book_author)
        val ivRemoveBook = itemView.findViewById<ImageView>(R.id.iv_delete_book)
        val parentLayout = itemView.findViewById<MaterialCardView>(R.id.parent_layout)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.remove_book_list, parent, false)

        return BooksViewHolder(view)
    }

    override fun onBindViewHolder(holder: BooksViewHolder, position: Int) {
        val book = bookList[position]
        holder.tvBookTitle.text = book.name
        holder.tvBookAuthor.text = book.author

        holder.ivRemoveBook.setOnClickListener {
            onDeleteClickListener?.let { click ->
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

    private var onDeleteClickListener: ((Book) -> Unit)? = null

    fun setOnDeleteClickListener(listener: (Book) -> Unit) {
        onDeleteClickListener = listener
    }

    private var onBookClickListener: ((Book) -> Unit)? = null

    fun setOnBookClickListener(listener: (Book) -> Unit) {
        onDeleteClickListener = listener
    }

}