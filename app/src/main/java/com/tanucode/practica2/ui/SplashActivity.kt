package com.tanucode.practica2.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.tanucode.practica2.MainActivity
import com.tanucode.practica2.R
import com.tanucode.practica2.auth.LoginActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        com.google.android.gms.maps.MapsInitializer.initialize(this)
        setTheme(R.style.Theme_Splatpoons_Splash) //Debe ir antes del super.onCreate
        super.onCreate(savedInstanceState)


        lifecycleScope.launch {
            delay(1500)
            val prefs = getSharedPreferences(
                "com.google.firebase.auth.authPreferences",
                Context.MODE_PRIVATE
            )
            Log.d("SplashDebug", "⚙️ authPreferences ALL = ${prefs.all}")

            val auth = FirebaseAuth.getInstance()

            val filesDir = applicationContext.filesDir
            val storeFile = File(filesDir, "auth.api.store")
            val cryptoFile = File(filesDir, "auth.api.crypto")

            Log.d("SplashDebug", "⚙️ auth.api.store exists=${storeFile.exists()} size=${storeFile.length()}")
            Log.d("SplashDebug", "⚙️ auth.api.crypto exists=${cryptoFile.exists()} size=${cryptoFile.length()}")
            Log.d("SplashDebug", "⏩ currentUser = ${auth.currentUser}")



            val next = if (auth.currentUser!=null){
                MainActivity::class.java
            }else{
                LoginActivity::class.java
            }

            startActivity(Intent(this@SplashActivity, next))
            finish()
        }

    }
}