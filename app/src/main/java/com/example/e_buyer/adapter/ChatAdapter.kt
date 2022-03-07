package com.example.e_buyer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_buyer.R
import com.example.e_buyer.model.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.makeramen.roundedimageview.RoundedImageView
import java.util.*

class ChatAdapter(private val context: Context, private val chatlist: ArrayList<Chat>) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    private val MESSAGE_TYPE_LEFT = 0
    private val MESSAGE_TYPE_Right = 1
    var firebaseUser: FirebaseUser? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == MESSAGE_TYPE_Right) {
            var view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_right, parent, false)
            return ViewHolder(view)
        } else {
            var view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_left, parent, false)
            return ViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat=chatlist[position]
        holder.txtUserName.text=chat.message
//        Glide.with(context).load(chat.userImage).placeholder(R.drawable.ic_back).into(holder.imgUser)
    }

    override fun getItemCount(): Int {
        return chatlist.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtUserName: TextView = view.findViewById(R.id.tvMessage)
        val imgUser: RoundedImageView = view.findViewById(R.id.userImage)
    }

    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        if (chatlist[position].senderId == firebaseUser!!.uid) {
            return MESSAGE_TYPE_Right
        } else {
            return MESSAGE_TYPE_LEFT
        }


    }
}