package com.example.songappfrontend

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val recyclerView = findViewById<RecyclerView>(R.id.profileSongsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // включаем стрелку "назад"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Профиль"

        // временные данные - потом заменим API
        val mySongs = listOf(
            Song("Мой кавер", "Believer"),
            Song("Мой кавер ", "Numb"),
            Song("Авторский трек", "Dreaming Sky")
        )

        val adapter = SongAdapter(mySongs)
        recyclerView.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish() // возвращаемся назад
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}


