package com.example.e_buyer.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.e_buyer.R
import com.example.e_buyer.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.activity_user.imageBack

class ProfileActivity : AppCompatActivity() {
    private lateinit var  firebaseUser: FirebaseUser
    private lateinit var  databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        firebaseUser=FirebaseAuth.getInstance().currentUser!!
        databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)

        databaseReference.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user=snapshot.getValue(User::class.java)
                userName.text=user!!.userName
                if(user.profileImage==""){
                    userImage.setImageResource(R.drawable.ic_back)
                }else{
                    Glide.with(this@ProfileActivity).load(user.profileImage).into(imgProfile)
                }
            }

            override fun onCancelled(error: DatabaseError) {
               Toast.makeText(applicationContext,error.message,Toast.LENGTH_SHORT).show()
            }

        })
        imageBack.setOnClickListener{
            onBackPressed()
        }
        imgProfile.setOnClickListener{
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}