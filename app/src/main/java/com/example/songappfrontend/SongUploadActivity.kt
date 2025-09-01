package com.example.songappfrontend

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody.Part
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException

class SongUploadActivity : AppCompatActivity() {

    private lateinit var songNameInput: EditText
    private lateinit var songAuthorInput: EditText
    private lateinit var selectFileButton: Button
    private lateinit var uploadButton: Button
    private lateinit var logoutButton: Button

    private var selectedFileUri: Uri? = null
    private val PICK_FILE_REQUEST = 1001

    private val client = OkHttpClient()
    private val baseUrl = "http://10.0.2.2:8000" // сервер Django
    private lateinit var accessToken: String
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_upload)

        songNameInput = findViewById(R.id.songNameInput)
        songAuthorInput = findViewById(R.id.songAuthorInput)
        selectFileButton = findViewById(R.id.selectFileButton)
        uploadButton = findViewById(R.id.uploadButton)
        logoutButton = findViewById(R.id.logoutButton)

        accessToken = intent.getStringExtra("ACCESS_TOKEN") ?: ""
        userId = intent.getIntExtra("ID", 0)

        selectFileButton.setOnClickListener {
            selectFile()
        }

        uploadButton.setOnClickListener {
            uploadSong()
        }

        logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun selectFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "audio/mpeg" // только mp3
        startActivityForResult(Intent.createChooser(intent, "Выберите MP3 файл"), PICK_FILE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FILE_REQUEST && resultCode == Activity.RESULT_OK) {
            selectedFileUri = data?.data
            if (selectedFileUri != null) {
                Toast.makeText(this, "Файл выбран", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadSong() {
        val name = songNameInput.text.toString()
        val author = songAuthorInput.text.toString()
        val fileUri = selectedFileUri

        if (name.isEmpty() || author.isEmpty() || fileUri == null) {
            Toast.makeText(this, "Заполните все поля и выберите файл", Toast.LENGTH_SHORT).show()
            return
        }

        val file = File(fileUri.path ?: "")
        val requestFile = file.asRequestBody("audio/mpeg".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("name", name)
            .addFormDataPart("author", author)
            .addFormDataPart("file", file.name, requestFile)
            .build()

        val request = Request.Builder()
            .url("$baseUrl/songs/") // URL загрузки
            .addHeader("Authorization", "Bearer $accessToken")
            .post(multipartBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@SongUploadActivity, "Ошибка сети: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@SongUploadActivity, "Песня загружена!", Toast.LENGTH_SHORT).show()
                        songNameInput.text.clear()
                        songAuthorInput.text.clear()
                        selectedFileUri = null
                    } else {
                        Toast.makeText(this@SongUploadActivity, "Ошибка загрузки: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun logout() {
        // Просто возвращаемся на экран логина
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
