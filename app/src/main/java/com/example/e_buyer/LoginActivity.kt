package com.example.e_buyer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.btnLogin_login
import kotlinx.android.synthetic.main.activiy_sign_up.*
import org.w3c.dom.Text

class LoginActivity : AppCompatActivity() {
    private lateinit var  auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
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
                            val intent = Intent(this,MainActivity::class.java)
                            startActivity(intent)
                        }
                        else{
                            Toast.makeText(applicationContext,"Email or password is wrong",Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
        btnSignup_login.setOnClickListener {
            val intent = Intent(this,SignupActivity::class.java)
            startActivity(intent)
        }
    }
}