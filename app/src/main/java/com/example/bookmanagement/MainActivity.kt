package com.example.bookmanagement

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL


class MainActivity : AppCompatActivity() {

    private lateinit var logoutBtn : Button
    private lateinit var auth : FirebaseAuth
    private var userAuth : FirebaseUser? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var bookAdapter: BookAdapter
    private val books = mutableListOf<Book>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logoutBtn = findViewById(R.id.logout_btn)
        recyclerView = findViewById(R.id.recyclerView)

        auth = FirebaseAuth.getInstance()
        userAuth = auth.currentUser


        if (userAuth == null) {
            changeActivities(applicationContext, LoginActivity::class.java, true)
        } else {
            val manager = LinearLayoutManager(this)
            recyclerView.layoutManager = manager
            recyclerView.setHasFixedSize(true)


            if (books.isEmpty()) {
                fetchDataFromApi()
            } else {
                bookAdapter = BookAdapter(books)
                recyclerView.adapter = bookAdapter
            }
        }


        logoutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            changeActivities(applicationContext, LoginActivity::class.java, true)
        }
    }

    private fun changeActivities(context: Context, activity: Class<out Activity>, finish: Boolean = false) {
        val intent = Intent(context, activity)
        val intentArray = arrayOf(intent)
        startActivities(intentArray)
        if (finish) finish()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun fetchDataFromApi() {
        GlobalScope.launch(Dispatchers.IO) {
            val url = URL("https://opendata.paris.fr/api/explore/v2.1/catalog/datasets/les-1000-titres-les-plus-reserves-dans-les-bibliotheques-de-pret/records?limit=100")
            val jsonStr = url.readText()
            val jsonObj = JSONObject(jsonStr)
            val resultsArray = jsonObj.getJSONArray("results")
            for (i in 0 until resultsArray.length()) {
                val bookObj = resultsArray.getJSONObject(i)
                val book = Book(
                    bookObj.getInt("rang"),
                    bookObj.optString("typeDeDocument"),
                    bookObj.getDouble("reservations"),
                    bookObj.getString("titre"),
                    bookObj.optString("auteur")
                )
                books.add(book)
            }
            withContext(Dispatchers.Main) {
                // Accéder à recyclerView et mettre à jour l'adaptateur sur le thread principal
                recyclerView = findViewById(R.id.recyclerView)
                recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                recyclerView.adapter = BookAdapter(books)
            }
        }
    }
}