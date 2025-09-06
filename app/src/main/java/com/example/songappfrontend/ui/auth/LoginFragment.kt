package com.example.songappfrontend.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.songappfrontend.R
import com.example.songappfrontend.util.SessionManager
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val client = OkHttpClient()
    // Эмулятор → Django на хосте
    private val baseUrl = "http://10.0.2.2:8000"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // если токен есть — сразу в список
        val session = SessionManager(requireContext())
        session.getAccessToken()?.let {
            findNavController().navigate(
                R.id.action_loginFragment_to_songListFragment
            )
            return
        }

        binding.loginButton.setOnClickListener {
            val login = binding.loginInput.text?.toString().orEmpty()
            val password = binding.passwordInput.text?.toString().orEmpty()
            if (login.isBlank() || password.isBlank()) {
                Toast.makeText(requireContext(), "Введите логин и пароль", Toast.LENGTH_SHORT).show()
            } else {
                loginRequest(login, password)
            }
        }

        binding.goRegisterButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_loginFragment_to_registerFragment
            )
        }
    }

    private fun loginRequest(login: String, password: String) {
        val json = JSONObject().apply {
            put("login", login)
            put("password", password)
        }

        val body = RequestBody.Companion.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            json.toString()
        )

        val request = Request.Builder()
            .url("$baseUrl/token/")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (!isAdded) return
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Ошибка сети", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (!isAdded) return
                val respBody = response.body?.string()
                requireActivity().runOnUiThread {
                    if (response.isSuccessful && respBody != null) {
                        val jsonResp = JSONObject(respBody)
                        val accessToken = jsonResp.getString("access")
                        val refreshToken = jsonResp.optString("refresh", null)
                        val userId = jsonResp.getInt("id")
                        val loginName = jsonResp.getString("login")

                        SessionManager(requireContext()).save(accessToken, refreshToken, userId, loginName)
                        Toast.makeText(requireContext(), "Привет, $loginName!", Toast.LENGTH_SHORT).show()

                        findNavController().navigate(
                            R.id.action_loginFragment_to_songListFragment
                        )
                    } else {
                        Toast.makeText(requireContext(), "Неверный логин или пароль", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}