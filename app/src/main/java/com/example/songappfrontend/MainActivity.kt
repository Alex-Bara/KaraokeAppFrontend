package com.example.songappfrontend

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        val recyclerView = findViewById<RecyclerView>(R.id.songRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // временные данные
        val songs = listOf(
            Song("Imagine Dragons - Believer"),
            Song("Queen - Bohemian Rhapsody"),
            Song("Metallica - Nothing Else Matters"),
            Song("Linkin Park - Numb")
        )

        val adapter = SongAdapter(songs)
        recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean { // <-- убрал "?"
        menuInflater.inflate(R.menu.top_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_profile -> {
                // TODO: переход в профиль
                true
            }
            R.id.action_settings -> {
                // TODO: открыть меню/настройки
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
