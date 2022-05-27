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
import com.example.librarymanagement.models.User
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class RequestedUsersAdapter(var context: Context, var userList: ArrayList<String>) :
    RecyclerView.Adapter<RequestedUsersAdapter.BooksViewHolder>() {

    private val usersCollection = FirebaseFirestore.getInstance().collection("user")

    inner class BooksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvUserName = itemView.findViewById<TextView>(R.id.txt_user_name)
        val tvEnrolNum = itemView.findViewById<TextView>(R.id.txt_enrol_num)
        val parentLayout = itemView.findViewById<MaterialCardView>(R.id.parent_layout)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_users, parent, false)

        return BooksViewHolder(view)
    }

    override fun onBindViewHolder(holder: BooksViewHolder, position: Int) {
        val uid = userList[position]
        usersCollection.document(uid).get().addOnCompleteListener {
            val user = it.result.toObject(User::class.java)!!

            holder.tvUserName.text = user.username
            holder.tvEnrolNum.text = user.enrolmentNumber


            holder.parentLayout.setOnClickListener {
                onUserClickListener?.let { click ->
                    click(user)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    private var onEditClickListener: ((User) -> Unit)? = null

    fun setOnEditClickListener(listener: (User) -> Unit) {
        onEditClickListener = listener
    }

    private var onRequestClickListener: ((User) -> Unit)? = null

    fun setonRequestClickListener(listener: (User) -> Unit) {
        onRequestClickListener = listener
    }

    private var onUserClickListener: ((User) -> Unit)? = null

    fun setOnUserClickListener(listener: (User) -> Unit) {
        onUserClickListener = listener
    }

}