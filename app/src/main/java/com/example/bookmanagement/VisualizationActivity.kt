package com.example.bookmanagement

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class VisualizationActivity : AppCompatActivity() {

    private lateinit var backBtn: Button
    private lateinit var rangView : TextView
    private lateinit var titleView : TextView
    private lateinit var reservationView : TextView
    private lateinit var authorView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualization)

        backBtn = findViewById(R.id.back_btn)

        backBtn.setOnClickListener {
            changeActivities(applicationContext, MainActivity::class.java, false)
        }

        // Récupérer l'Intent qui a démarré cette activité
        val intent = intent

        if (intent.hasExtra("BOOK_RANG") && intent.hasExtra("BOOK_TITLE")
            && intent.hasExtra("BOOK_RESERVATION") && intent.hasExtra("BOOK_AUTHOR")) {
            val bookRang = intent.getIntExtra("BOOK_RANG", -1)
            val bookTitle = intent.getStringExtra("BOOK_TITLE")
            val bookAuthor = intent.getStringExtra("BOOK_AUTHOR")
            val bookReservation = intent.getDoubleExtra("BOOK_RESERVATION", 0.0)

            rangView = findViewById(R.id.book_rang)
            titleView = findViewById(R.id.book_title)
            reservationView = findViewById(R.id.book_reservation)
            authorView = findViewById(R.id.book_author)

            rangView.text = getString(R.string.book_rang, bookRang)
            titleView.text = getString(R.string.book_title, bookTitle)
            reservationView.text = getString(R.string.book_reservation, bookReservation)
            authorView.text = getString(R.string.book_author, bookAuthor)

        }

    }

    private fun changeActivities(context: Context, activity: Class<out Activity>, finish: Boolean) {
        val intent = Intent(context, activity)
        val intentArray = arrayOf(intent)
        startActivities(intentArray)
        if (finish) finish()
    }
}