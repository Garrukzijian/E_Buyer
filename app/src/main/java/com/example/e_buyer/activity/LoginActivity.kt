package com.example.e_buyer.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.e_buyer.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.btnLogin_login
import kotlinx.android.synthetic.main.activiy_sign_up.*

class LoginActivity : AppCompatActivity() {
    private lateinit var  auth: FirebaseAuth
    private lateinit var  firebaseUser: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        firebaseUser=auth.currentUser!!
        if(firebaseUser!=null){
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
            finish()
        }
        btnLogin_login.setOnClickListener {
            val email_login= etEmail.text.toString()
            val password_login = etpassword_login.text.toString()
            if (TextUtils.isEmpty(email_login)&&TextUtils.isEmpty(password_login)){
                Toast.makeText(applicationContext,"Email and password are requried",Toast.LENGTH_SHORT).show()
            }
            else{
                auth.signInWithEmailAndPassword(email_login,password_login)
                    .addOnCompleteListener(this){
                        if (it.isSuccessful){
                            etEmail_login.setText("")
                            etpassword_login.setText("")
                            val intent = Intent(this, UserActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else{
                            Toast.makeText(applicationContext,"Email or password is wrong",Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
        btnSignup_login.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}