package com.example.songappfrontend.api

data class LoginRequest(val login: String, val password: String)
data class TokenResponse(val access: String, val refresh: String)