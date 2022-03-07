package com.example.e_buyer.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.e_buyer.R
import com.example.e_buyer.adapter.UserAdapter
import com.example.e_buyer.firebase.FirebaseService
import com.example.e_buyer.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.activity_user.imageBack
import java.util.ArrayList


class UserActivity : AppCompatActivity() {
    var  userlist=ArrayList<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        FirebaseService.sharePref =getSharedPreferences("sharedPref",Context.MODE_PRIVATE)
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener{
            FirebaseService.token=it.token

        }

        userRecyclerView.layoutManager=LinearLayoutManager(this,LinearLayout.VERTICAL,false)

        var userAdapter =UserAdapter(this,userlist)
        userRecyclerView.adapter=userAdapter
        imageBack.setOnClickListener{
            onBackPressed()
        }
        imgProfile.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        getUserList()
    }
    fun getUserList(){
        var firebase =FirebaseAuth.getInstance().currentUser!!
        var databaseReference= FirebaseDatabase.getInstance().getReference("users")
        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,error.message,Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                userlist.clear()
                val currentUser=snapshot.getValue(User::class.java)
                if(currentUser!!.profileImage==""){
                    imgProfile.setImageResource(R.drawable.ic_back)
                }else{
                    Glide.with(this@UserActivity).load(currentUser.profileImage).into(imgProfile)
                }
                for(dataSnapShot:DataSnapshot in snapshot.children){
                    val user=dataSnapShot.getValue(User::class.java)
                    if(!user!!.userId.equals(firebase.uid)){

                        userlist.add(user)
                    }
                }
                val userAdapter=UserAdapter(this@UserActivity,userlist)
                userRecyclerView.adapter = userAdapter

            }

        })
    }
}