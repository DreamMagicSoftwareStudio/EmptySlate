package com.dream_magic.emptyslate.views

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dream_magic.emptyslate.R
import java.security.MessageDigest

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init();
    }

    private fun init(){
       main()
        navigateToSignIn();
    }

    private fun navigateToSignIn() {
        // Delay navigation for 3 seconds
        Handler().postDelayed({
            // Create an intent to navigate to the second activity
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            // Optionally, finish the current activity if you don't want to return to it
            finish()
        }, 3000) // 3000 milliseconds = 3 seconds
    }

    fun generateSHA1(input: String): String {
        val md = MessageDigest.getInstance("SHA-1")
        val messageDigest = md.digest(input.toByteArray())
        return messageDigest.fold("") { str, it -> str + "%02x".format(it) }
    }

    fun main() {
        val input = "empty_slate"
        val sha1Hash = generateSHA1(input)
        println("SHA-1 Hash: $sha1Hash")
    }
}