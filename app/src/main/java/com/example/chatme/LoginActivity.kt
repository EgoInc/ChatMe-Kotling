package com.example.chatme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {
//    val goToRegistration: TextView = findViewById(R.id.goToRegistration)

    var firebaseUser: FirebaseUser? = null
    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        goToRegistration.setOnClickListener{
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
        mAuth = FirebaseAuth.getInstance()
        login_button.setOnClickListener{
            loginUser()
        }
    }

    private fun loginUser() {
        val email: String = loginEmail.text.toString()
        val password: String = loginPassword.text.toString()
        if (email == "" || password == ""){
            Toast.makeText(this, "Lines can't be empty", Toast.LENGTH_LONG).show()
        }
        else{
           mAuth.signInWithEmailAndPassword(email, password)
               .addOnCompleteListener{ task ->
                   if(task.isSuccessful){
                       val intent = Intent(this, MainActivity::class.java)
                       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                       startActivity(intent)
                       finish()
                   }
                   else{
                       Toast.makeText(this, "Error:"+task.exception.toString(),Toast.LENGTH_LONG).show()
                   }
               }
        }
    }

    override fun onStart() {
        super.onStart()
        
        firebaseUser = FirebaseAuth.getInstance().currentUser
        
        if(firebaseUser!=null){
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}