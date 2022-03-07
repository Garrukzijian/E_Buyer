package com.example.e_buyer.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_buyer.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activiy_sign_up.*

class SignupActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activiy_sign_up)
        auth= FirebaseAuth.getInstance()
        btnSignup.setOnClickListener{
            val userName = etName.text.toString()
            val emailaddress=etEmail.text.toString()
            val password= etpassword.text.toString()
            val phonenumber=etphonenumber.text.toString()
            val confimpassword=etConfirmpassword.text.toString()
            if(userName.isEmpty()){
                Toast.makeText(applicationContext,"username is required",Toast.LENGTH_SHORT).show()
            }
            if(emailaddress.isEmpty()){
                Toast.makeText(applicationContext,"email is required",Toast.LENGTH_SHORT).show()
            }
            if(phonenumber.isEmpty()){
                Toast.makeText(applicationContext,"phone number is required",Toast.LENGTH_SHORT).show()
            }
            if(password.isEmpty()){
                Toast.makeText(applicationContext,"password is required",Toast.LENGTH_SHORT).show()
            }
            if(confimpassword.isEmpty()){
                Toast.makeText(applicationContext,"confimpassword is required",Toast.LENGTH_SHORT).show()
            }
            if(password != confimpassword){
                Toast.makeText(applicationContext,"password is not same",Toast.LENGTH_SHORT).show()
            }
            registerUser(userName,emailaddress,phonenumber,password)
        }
        btnLogin.setOnClickListener {
            val intent=Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun registerUser(username:String,email:String,phonenumber:String,password:String){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){
                if(it.isSuccessful){
                    val user:FirebaseUser? =auth.currentUser
                    val userId:String =user!!.uid

                    databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(userId)
                    val hashMap:HashMap<String,String> = HashMap()
                    hashMap.put("userId",userId)
                    hashMap.put("userName",username)
                    hashMap.put("phonenumber",phonenumber)
                    hashMap.put("profileImage","")

                    databaseReference.setValue(hashMap).addOnCompleteListener(this){
                        if(it.isSuccessful){
                            //open home activity
                            etName.setText("")
                            etEmail.setText("")
                            etphonenumber.setText("")
                            etpassword.setText("")
                            etConfirmpassword.setText("")
                            var intent = Intent(this, UserActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
    }
}