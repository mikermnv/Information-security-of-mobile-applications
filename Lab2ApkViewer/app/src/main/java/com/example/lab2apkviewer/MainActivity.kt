package com.example.lab2apkviewer

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.lab2apkviewer.databinding.ActivityMainBinding
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.secretTextView.text = loadSecretText()
    }

    private fun loadSecretText(): String {
        return try {
            assets.open("secret.txt").bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            Log.d("Secret viewer app", e.message.toString())
            "Secret not found: ${e.message}"
        }
    }
}