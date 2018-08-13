package com.example.abhi.appster

import android.content.Intent
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.widget.Button

/**
 * Kotlin app keeps track of user form input of job applications. CRUD operations and storage
 * performed through Firebase Realtime Database.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val jobAppButton = findViewById<Button>(R.id.jobAppButton)
        val viewAppButton = findViewById<Button>(R.id.viewAppButton)

        var typeface : Typeface? = ResourcesCompat.getFont(this.applicationContext, R.font.roboto_medium)
        jobAppButton!!.setTypeface(typeface, Typeface.NORMAL)
        viewAppButton!!.setTypeface(typeface, Typeface.NORMAL)

        jobAppButton.setOnClickListener {
            val intent = Intent(this, JobEntry::class.java)
            startActivity(intent)
        }

        viewAppButton.setOnClickListener {
            val intent = Intent(this, ApplicationListView::class.java)
            startActivity(intent)
        }
    }
}
