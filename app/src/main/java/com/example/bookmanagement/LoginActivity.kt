package com.example.bookmanagement

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var emailInput : EditText
    private lateinit var passwordInput : EditText
    private lateinit var loginBtn : Button
    private lateinit var mAuth : FirebaseAuth
    private lateinit var textView: TextView
    private lateinit var progressBar: ProgressBar

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            changeActivities(applicationContext, MainActivity::class.java, true)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()

        emailInput = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.password_input)
        loginBtn = findViewById(R.id.login_btn)
        textView = findViewById(R.id.register_now)
        progressBar = findViewById(R.id.progress_bar)

        loginBtn.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val EMAIL = emailInput.text.toString()
            val PASSWORD = passwordInput.text.toString()

            println("$EMAIL/$PASSWORD")

            if (TextUtils.isEmpty(EMAIL)) {
                Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(PASSWORD)) {
                Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            mAuth.signInWithEmailAndPassword(EMAIL, PASSWORD)
                .addOnCompleteListener(this) { task ->
                    progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = mAuth.currentUser

                        Toast.makeText(
                            baseContext,
                            "Login successful",
                            Toast.LENGTH_SHORT
                            ).show()

                        changeActivities(applicationContext, MainActivity::class.java, true)

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }



        }

        textView.setOnClickListener {
            changeActivities(applicationContext, RegisterActivity::class.java, true)
        }




    }

    private fun changeActivities(context: Context, activity: Class<out Activity>, finish: Boolean) {
        val intent = Intent(context, activity)
        val intentArray = arrayOf(intent)
        startActivities(intentArray)
        if (finish) finish()
    }
}