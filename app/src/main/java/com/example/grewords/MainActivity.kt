package com.example.grewords

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.data.model.PhoneNumber
import com.firebase.ui.auth.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class MainActivity : AppCompatActivity() {
    private var firebaseAuth=FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        alreadyLoggedIn(firebaseAuth.currentUser)
    }

    private fun alreadyLoggedIn(currentUser: FirebaseUser?) {
        if(currentUser!=null)
        {
            if(currentUser.isEmailVerified) {
                startActivity(Intent(this, MainPage::class.java))
                finish()
            }
        }

    }

    fun login(view: View)
    { // Login activity go
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
    fun register(view: View)
    {

        val userEmailid=findViewById<EditText>(R.id.user_email_Id)
        val userPassword=findViewById<EditText>(R.id.user_password)
        val phoneNumber=findViewById<EditText>(R.id.user_phonenumber).toString() // to use.
        var validatepassword=1
        var uppercase_count=0
        var lowercase_count=0
        var specialchr_count=0
        for(c in userPassword.text.toString())
        {
            if(c>='a'&&c<='z')
                lowercase_count++
            else if(c>='A'&&c<='Z')
                uppercase_count++
            else
                specialchr_count++
        }
        if(lowercase_count==0||uppercase_count==0||specialchr_count==0)
            validatepassword=0
        if(userEmailid.text.toString().isEmpty())
        { // In case the user emailid entered is empty
            userEmailid.error="Please enter a valid Email"
            userEmailid.requestFocus()
            return
        }
        if(userPassword.text.toString().length<6||validatepassword==0) // password strength
        {
            userPassword.error="Please enter a strong password with more than 6 characters with atleast one uppercase, on lowercase and one special character"
            userPassword.requestFocus()
            return

        }
        firebaseAuth.createUserWithEmailAndPassword(userEmailid.text.toString(), userPassword.text.toString())
            .addOnCompleteListener(this){ task->
                if(task.isSuccessful)
                {
                    val currentUser=firebaseAuth.currentUser
                    currentUser?.sendEmailVerification() //send user verification email
                        ?.addOnCompleteListener{ task1 ->
                            if(task1.isSuccessful)
                            {
                                // Login activity go
                                createList()
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }
                            
                        }

                }
                else
                {
                    Toast.makeText(this, "Try again after some time", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun createList(){
        val scan = Scanner(
                resources.openRawResource(R.raw.grewords))
        readFileHelper(scan)
    }
    private fun readFileHelper(scan: Scanner){
        while(scan.hasNextLine()){
            val line: String = scan.nextLine()
            val parts = line.split(":")
            if(parts.size<2) continue

            if(FirebaseAuth.getInstance().currentUser == null){
                Log.d("debug", "what the hell")
            }else if(FirebaseAuth.getInstance().currentUser !=null){
                var root = FirebaseDatabase.getInstance().getReference()
                root.child("users").child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .child("stack0").child(parts[0]).setValue(parts[1])
            }
        }
    }

}