package com.example.songappfrontend

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Тут можно хранить токен после логина в SharedPreferences
        val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val token = sharedPref.getString("auth_token", null)

        if (token.isNullOrEmpty()) {
            // Если токена нет → показываем логин
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            // Если токен есть → кидаем в загрузку песен
            startActivity(Intent(this, SongUploadActivity::class.java))
        }

        finish() // закрываем MainActivity, чтобы не вернуться назад
    }
}
