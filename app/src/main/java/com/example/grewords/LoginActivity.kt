package com.example.grewords

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    private var firebaseAuth =FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun onStart() {
        super.onStart()
        val cur=firebaseAuth.currentUser
        alreadyLoggedIn(cur)
    }

    fun userSignIn(view :View)
    {

        val userEmailid=findViewById<EditText>(R.id.user_emailid)
        val userPassword=findViewById<EditText>(R.id.user_login_password)

        if(userEmailid.text.toString().isEmpty())
        {
            userEmailid.error="Please enter a valid Email"

        }
        if(userPassword.text.toString().length<6)
        {
            userPassword.error="Please enter a strong password with more than 6 characters"

        }
        firebaseAuth.signInWithEmailAndPassword(userEmailid.text.toString(),userPassword.text.toString())
            .addOnCompleteListener(this){task ->
                if(task.isSuccessful)
                {
                    val currentUser=firebaseAuth.currentUser
                    alreadyLoggedIn(currentUser)
                }
                else
                {
                    Toast.makeText(this,"Wrong Email or Password",Toast.LENGTH_LONG).show()
                }
            }
    }
    private fun alreadyLoggedIn(currentUser:FirebaseUser?)
    {
        if(currentUser!=null)
        {
            if(currentUser.isEmailVerified) {
                startActivity(Intent(this, MainPage::class.java))
                finish()
            }
            else
            {
                Toast.makeText(this,"Verify your email",Toast.LENGTH_LONG).show()
            }
        }
        else
        {
            Toast.makeText(this,"Try again later",Toast.LENGTH_LONG).show()
        }
    }

}