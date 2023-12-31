package com.example.mancingku.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler;
import com.example.mancingku.R
import com.google.firebase.auth.FirebaseAuth


class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        val currentUser = FirebaseAuth.getInstance().currentUser

        Handler().postDelayed({
            val intent = if (currentUser != null) {
                // Pengguna sudah masuk sebelumnya, arahkan ke MainActivity
                Intent(this@Splash, MainActivity::class.java)

            } else {
                // Pengguna belum masuk sebelumnya, arahkan ke LoginActivity
                Intent(this@Splash, Login::class.java)
            }
            startActivity(intent)
            finish()
        }, 3000)
    }
}
