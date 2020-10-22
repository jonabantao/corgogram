package com.abantaoj.corgogram.ui.feed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.abantaoj.corgogram.R
import com.abantaoj.corgogram.ui.compose.ComposeActivity

class FeedActivity : AppCompatActivity() {
    private lateinit var buttonNewPost: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        buttonNewPost = findViewById(R.id.feedNewPostButton)

        buttonNewPost.setOnClickListener {
            navigateToComposeActivity()
        }
    }

    private fun navigateToComposeActivity() {
        startActivity(Intent(this, ComposeActivity::class.java))
    }
}