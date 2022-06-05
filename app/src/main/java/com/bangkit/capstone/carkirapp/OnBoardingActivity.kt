package com.bangkit.capstone.carkirapp

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.bangkit.capstone.carkirapp.data.local.datastore.DataPreference
import com.bangkit.capstone.carkirapp.databinding.ActivityOnBoardingBinding
import com.bangkit.capstone.carkirapp.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupFullScreen()

        val preferences = DataPreference.getInstance(dataStore)

        CoroutineScope(Dispatchers.Default).launch {
            val isFirstTime = preferences.loadStateFirstTime().first()

            // If not first time redirect to MainActivity
            if (!isFirstTime) {
                moveToMainActivity(); return@launch
            }

            // If is first time hold on this activity
            // and if user click the button get started it will update the state on datastore
            // then redirect to MainActivity and remove clear stack for user cannot back to on boarding page
            binding.btnGetStarted.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    preferences.updateStateFirstTime(!isFirstTime)
                    moveToMainActivity()
                }
            }
        }
    }

    private fun moveToMainActivity() {
        val intent = Intent(this@OnBoardingActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    @Suppress("DEPRECATION")
    private fun setupFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}