package com.example.e_buyer.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.e_buyer.R
import com.example.e_buyer.Retrofitlnstance
import com.example.e_buyer.adapter.ChatAdapter
import com.example.e_buyer.adapter.UserAdapter
import com.example.e_buyer.model.Chat
import com.example.e_buyer.model.NotificationData
import com.example.e_buyer.model.PushNotification
import com.example.e_buyer.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.activity_user.imgProfile
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap

class ChatActivity : AppCompatActivity() {
    var firebaseUser: FirebaseUser? = null
    var refernce: DatabaseReference? = null
    var chatlist = ArrayList<Chat>()
    var topic =""
//    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        chatRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)


        var intent = getIntent()
        var userId = intent.getStringExtra("userId")
        var userName = intent.getStringExtra("userName")
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refernce = FirebaseDatabase.getInstance().getReference("Users").child(userId!!)
        imageBack_chat.setOnClickListener {
            onBackPressed()
        }
        refernce!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                tvUserName.text = user!!.userName
                if (user.profileImage == "") {
                    imgProfile.setImageResource(R.drawable.ic_back)
                } else {
                    Glide.with(this@ChatActivity).load(user.profileImage).into(imgProfile)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        btnSendMessage.setOnClickListener {
            var message: String = etMessage.text.toString()

            if (message.isEmpty()) {
                Toast.makeText(applicationContext, "message is empty", Toast.LENGTH_SHORT).show()
            } else {
                sendMessage(firebaseUser!!.uid, userId, message)
                etMessage.setText("")
                topic="/topics/$userId"
                PushNotification(NotificationData(userName!!,message)
                    ,topic).also {
                        sendNotification(it)

                }
            }
        }
        readMessage(firebaseUser!!.uid, userId)
    }

    private fun sendMessage(senderId: String, receiverId: String, message: String) {
        var refernce: DatabaseReference? = FirebaseDatabase.getInstance().getReference()
        var hashMap: HashMap<String, String> = HashMap()
        hashMap.put("sendId", senderId)
        hashMap.put("reciverId", receiverId)
        hashMap.put("message", message)
        refernce!!.child("Chat").push().setValue(hashMap)
    }

    fun readMessage(senderId: String, receiverId: String) {
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Chat")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatlist.clear()
                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val chat = dataSnapShot.getValue(Chat::class.java)
                    if (chat!!.senderId.equals(senderId) && chat.reciverId.equals(receiverId) ||
                        chat.senderId.equals(receiverId) && chat.reciverId.equals(senderId)
                    ) {
                        chatlist.add(chat)
                    }
                }
                val chatAdapter = ChatAdapter(this@ChatActivity, chatlist)
                chatRecyclerView.adapter = chatAdapter

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun sendNotification(notification: PushNotification)= CoroutineScope(Dispatchers.IO).launch {
        try {
            val response =Retrofitlnstance.api.postNotification(notification)
            if(response.isSuccessful){
                Toast.makeText(this@ChatActivity,"Response ${Gson().toJson(response)}",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this@ChatActivity,response.errorBody().toString(),Toast.LENGTH_SHORT).show()
            }
        }catch (e:Exception){
            Toast.makeText(this@ChatActivity,e.message,Toast.LENGTH_SHORT).show()

        }
    }
}