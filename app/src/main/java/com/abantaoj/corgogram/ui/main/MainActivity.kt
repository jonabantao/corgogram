package com.abantaoj.corgogram.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.abantaoj.corgogram.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupBottomNavigationView()
    }

    private fun setupBottomNavigationView() {
        val fragmentManager: FragmentManager = supportFragmentManager

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            val fragment: Fragment = when (it.itemId) {
                R.id.actionHome -> FeedFragment.newInstance()
                R.id.actionCompose -> ComposeFragment.newInstance()
                R.id.actionProfile -> ProfileFragment.newInstance()
                else -> FeedFragment.newInstance()
            }
            fragmentManager.beginTransaction().replace(R.id.mainActivityFrameLayout, fragment)
                .commit()

            true
        }
        bottomNavigationView.selectedItemId = R.id.actionHome
    }
}