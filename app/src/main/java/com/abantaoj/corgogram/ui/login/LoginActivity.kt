package com.abantaoj.corgogram.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.abantaoj.corgogram.R
import com.abantaoj.corgogram.ui.feed.FeedActivity
import com.parse.LogInCallback
import com.parse.ParseUser
import com.parse.SignUpCallback

class LoginActivity : AppCompatActivity() {

    lateinit var editTextUsername: EditText
    lateinit var editTextPassword: EditText
    lateinit var buttonLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ParseUser.getCurrentUser() != null) {
            navigateToFeedActivity()
            return
        }

        setContentView(R.layout.activity_login)

        editTextUsername = findViewById(R.id.editTextUsername)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)

        buttonLogin.setOnClickListener {
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()
            login(username, password)
        }
    }

    private fun login(username: String, password: String) {
        val user = ParseUser()
        user.setPassword(password)
        user.username = username

        user.signUpInBackground(SignUpCallback { e ->
            if (e == null) {
                navigateToFeedActivity()
            }
        })

//        ParseUser.logInInBackground(username, password, LogInCallback { _, e ->
//            if (e != null) {
//
//                Toast.makeText(this, "Login Failed!", Toast.LENGTH_SHORT).show()
//                return@LogInCallback
//            }
//
//            navigateToFeedActivity()
//        })
    }

    private fun navigateToFeedActivity() {
        startActivity(Intent(this, FeedActivity::class.java))
    }
}