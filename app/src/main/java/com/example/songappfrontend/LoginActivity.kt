package com.example.songappfrontend

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var loginButton: MaterialButton
    private lateinit var registerButton: MaterialButton
    private lateinit var googleSignInButton: SignInButton

    private lateinit var googleSignInClient: GoogleSignInClient

    private val client = OkHttpClient()
    private val baseUrl = "http://10.0.2.2:8000"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.loginButton)
        registerButton = findViewById(R.id.registerButton)
        googleSignInButton = findViewById(R.id.googleSignInButton)

        // Настройка Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Введите email и пароль", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginRequest(email, password)
        }

        registerButton.setOnClickListener {
            // Переход на экран регистрации
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        googleSignInButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, 1002)
        }
    }

    private fun loginRequest(email: String, password: String) {
        val json = JSONObject()
        json.put("login", email) // если на бэке используем login как email
        json.put("password", password)

        val body = json.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val request = Request.Builder()
            .url("$baseUrl/token/")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@LoginActivity, "Ошибка сети", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val respBody = response.body?.string()
                runOnUiThread {
                    if (response.isSuccessful && respBody != null) {
                        val jsonResp = JSONObject(respBody)
                        val accessToken = jsonResp.getString("access")
                        val userId = jsonResp.getInt("id")
                        val loginName = jsonResp.getString("login")

                        Toast.makeText(this@LoginActivity, "Привет, $loginName!", Toast.LENGTH_SHORT).show()

                        // Переход к SongUploadActivity или Главной
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra("ACCESS_TOKEN", accessToken)
                        intent.putExtra("USER_ID", userId)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Неверный логин или пароль", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1002) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(Exception::class.java)
                Toast.makeText(this, "Google вход: ${account?.email}", Toast.LENGTH_SHORT).show()
                // TODO: Отправить token на бэкенд для авторизации/регистрации
            } catch (e: Exception) {
                Toast.makeText(this, "Ошибка Google Sign-In", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
