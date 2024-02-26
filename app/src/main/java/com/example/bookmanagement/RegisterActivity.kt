package com.example.bookmanagement

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var emailInput : EditText
    private lateinit var passwordInput : EditText
    private lateinit var passwordRepeatInput : EditText
    private lateinit var registerBtn : Button
    private lateinit var mAuth : FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private lateinit var textView: TextView


    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            val intentArray = arrayOf(intent)
            startActivities(intentArray)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth = FirebaseAuth.getInstance()

        emailInput = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.password_input)
        passwordRepeatInput = findViewById(R.id.password_repeat_input)
        registerBtn = findViewById(R.id.register_btn)
        progressBar = findViewById(R.id.progress_bar)
        textView = findViewById(R.id.login_now)

        registerBtn.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val EMAIL = emailInput.text.toString()
            val PASSWORD = passwordInput.text.toString()
            val PASSWORD_REPEAT = passwordRepeatInput.text.toString()

            if (TextUtils.isEmpty(EMAIL)) {
                Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(PASSWORD)) {
                Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                return@setOnClickListener
            }

            if (PASSWORD != PASSWORD_REPEAT) {
                Toast.makeText(this, "Not the same passwords", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                return@setOnClickListener
            }

            mAuth.createUserWithEmailAndPassword(EMAIL, PASSWORD)
                .addOnCompleteListener(this) { task ->
                    progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information

                        val user = mAuth.currentUser

                        Toast.makeText(
                            baseContext,
                            "Account created.",
                            Toast.LENGTH_SHORT,
                        ).show()

                        changeActivities(applicationContext, LoginActivity::class.java, false)
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }
        textView.setOnClickListener {
            changeActivities(applicationContext, LoginActivity::class.java, false)
        }

    }

    private fun changeActivities(context: Context, activity: Class<out Activity>, finish: Boolean) {
        val intent = Intent(context, activity)
        val intentArray = arrayOf(intent)
        startActivities(intentArray)
        if (finish) finish()
    }
}