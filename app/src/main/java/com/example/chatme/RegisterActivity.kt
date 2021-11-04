package com.example.chatme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUsers: DatabaseReference
    private var firebaseUserID: String = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        goToAuthorization.setOnClickListener{
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


        mAuth = FirebaseAuth.getInstance()
        register_button.setOnClickListener{
            registerUser()
        }


    }

    private fun registerUser() {
        val username: String = registerUsername.text.toString()
        val email: String = registerEmail.text.toString()
        val password: String = registerPassword.text.toString()

        if (username == "" || email == "" || password == ""){
            Toast.makeText(this, "Lines can't be empty", Toast.LENGTH_LONG).show()
        }
        else{
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{task ->
                if (task.isSuccessful){
                    firebaseUserID = mAuth.currentUser!!.uid
                    refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)

                    val userHashMap = HashMap<String, Any>()
                    userHashMap["uid"] = firebaseUserID
                    userHashMap["username"] = username
                    userHashMap["profile_img"] = "https://firebasestorage.googleapis.com/v0/b/chatme-kotlin-621f5.appspot.com/o/profile.png?alt=media&token=286bc408-c610-4fac-bb2d-3036f18609bd"
                    userHashMap["status"] = "offline"
                    userHashMap["search"] = username.lowercase() //чтобы поиск по имени не учитывал регистр
                    userHashMap["website"] = "https://www.youtube.com/watch?v=gfo2xZ2SMjc"

                    refUsers.updateChildren(userHashMap)
                        .addOnCompleteListener{task ->
                            if (task.isSuccessful){
                                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
                            }
                        }


                }
                else{
                    Toast.makeText(this, "Error:"+task.exception.toString(),Toast.LENGTH_LONG).show()
                }
            }
        }

    }
}