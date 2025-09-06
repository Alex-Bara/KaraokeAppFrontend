package com.example.songappfrontend

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val title = intent.getStringExtra("title")
        val author = intent.getStringExtra("author")

        val songTitle = findViewById<TextView>(R.id.playerSongTitle)
        val songLyrics = findViewById<TextView>(R.id.playerLyrics)

        songTitle.text = "$title — $author"
        songLyrics.text = "Тут будут слова песни..."
    }
}
