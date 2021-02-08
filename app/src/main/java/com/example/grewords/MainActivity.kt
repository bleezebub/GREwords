package com.example.grewords

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
     private var fb=FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun login(view:View)
    {
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }
    fun register(view:View)
    {

        val em=findViewById<EditText>(R.id.usr)
        val pas=findViewById<EditText>(R.id.pass)
        val ph=findViewById<EditText>(R.id.pho)
        if(em.text.toString().isEmpty())
        {
            em.error="Please enter a valid Email"
            em.requestFocus()
            return
        }
        if(pas.text.toString().length<6)
        {
            pas.error="Please enter a strong password with more than 6 characters"
            pas.requestFocus()
            return

        }
        fb.createUserWithEmailAndPassword(em.text.toString(),pas.text.toString())
            .addOnCompleteListener(this){task->
                if(task.isSuccessful)
                {
                    val user=fb.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener{task1 ->
                            if(task1.isSuccessful)
                            {
                                startActivity(Intent(this,LoginActivity::class.java))
                                finish()
                            }
                            
                        }

                }
                else
                {
                    Toast.makeText(this,"Try again after some time",Toast.LENGTH_LONG).show()
                }
            }
    }
}