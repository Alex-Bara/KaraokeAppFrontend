package com.example.songappfrontend.util

import android.content.Context
import android.content.Context.MODE_PRIVATE

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("auth", MODE_PRIVATE)

    fun save(access: String, refresh: String?, id: Int, login: String) {
        prefs.edit()
            .putString("access", access)
            .putString("refresh", refresh)
            .putInt("id", id)
            .putString("login", login)
            .apply()
    }

    fun getAccessToken(): String? = prefs.getString("access", null)

    fun clear() = prefs.edit().clear().apply()
}
