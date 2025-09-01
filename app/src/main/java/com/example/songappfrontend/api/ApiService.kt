package com.example.songappfrontend.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("token/")
    fun login(@Body request: LoginRequest): Call<TokenResponse>
}
