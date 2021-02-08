package com.example.grewords

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    private var fb =FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val sa=findViewById<Button>(R.id.sign_up)
        sa.setOnClickListener{
            poo()
        }
    }

    override fun onStart() {
        super.onStart()
        val cur=fb.currentUser
        godash(cur)
    }
    fun poo()
    {

        val em=findViewById<EditText>(R.id.usr)
        val pas=findViewById<EditText>(R.id.pass)

        if(em.text.toString().isEmpty())
        {
            em.error="Please enter a valid Email"

        }
        if(pas.text.toString().length<6)
        {
            pas.error="Please enter a strong password with more than 6 characters"

        }
        fb.signInWithEmailAndPassword(em.text.toString(),pas.text.toString())
            .addOnCompleteListener(this){task ->
                if(task.isSuccessful)
                {
                    val usr=fb.currentUser
                    godash(usr)
                }
                else
                {
                    Toast.makeText(this,"Wrong Email or Password",Toast.LENGTH_LONG).show()
                }
            }
    }
    fun godash(usr:FirebaseUser?)
    {
        if(usr!=null)
        {
            if(usr.isEmailVerified) {
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