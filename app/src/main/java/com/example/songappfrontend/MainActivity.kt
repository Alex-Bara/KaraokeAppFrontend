package com.example.songappfrontend

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.songappfrontend.databinding.ActivityMainBinding
import com.example.songappfrontend.util.SessionManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment)

        // Тулбар подключаем к NavController
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.songListFragment) // топ-уровень
        )
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        // Обработка кликов по меню тулбара — делаем в фрагментах (см. SongListFragment)
        // Здесь — только системная кнопка "назад" (если нужна дополнительная логика)

        // Если токен уже есть — прыгаем сразу в список
        if (SessionManager(this).getAccessToken() != null &&
            navController.currentDestination?.id == R.id.loginFragment
        ) {
            navController.navigate(R.id.action_loginFragment_to_songListFragment)
        }

        // Аппаратная кнопка "назад": пусть работает по графу
        onBackPressedDispatcher.addCallback(this) { navController.popBackStack() }
    }
}
