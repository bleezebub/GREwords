package com.example.grewords

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
     private var firebaseAuth=FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun login(view:View)
    { // Login activity go
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }
    fun register(view:View)
    {

        val userEmailid=findViewById<EditText>(R.id.user_email_Id)
        val userPassword=findViewById<EditText>(R.id.user_password)
        val userPhonenumber=findViewById<EditText>(R.id.user_phonenumber) // to use.

        if(userEmailid.text.toString().isEmpty())
        { // In case the user emailid entered is empty
            userEmailid.error="Please enter a valid Email"
            userEmailid.requestFocus()
            return
        }
        if(userPassword.text.toString().length<6) // password strength
        {
            userPassword.error="Please enter a strong password with more than 6 characters"
            userPassword.requestFocus()
            return

        }
        firebaseAuth.createUserWithEmailAndPassword(userEmailid.text.toString(),userPassword.text.toString())
            .addOnCompleteListener(this){task->
                if(task.isSuccessful)
                {
                    val current_user=firebaseAuth.currentUser
                    current_user?.sendEmailVerification() //send user verification email
                        ?.addOnCompleteListener{task1 ->
                            if(task1.isSuccessful)
                            {
                                // Login activity go
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