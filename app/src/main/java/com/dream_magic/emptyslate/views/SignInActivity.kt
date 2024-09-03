package com.dream_magic.emptyslate.views

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dream_magic.emptyslate.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException

class SignInActivity : AppCompatActivity() {

    private var mSignInButton: Button? = null;
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private val REQ_ONE_TAP = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init();
    }

    private fun init() {
        val signIn: Button = findViewById(R.id.bt_signin)
        initiateGoogleClient();
        signIn.setOnClickListener {
//            Toast.makeText(this, "Signing In...", Toast.LENGTH_SHORT).show()
            performSignIn();
        }
    }

    private fun initiateGoogleClient() {
        // Initialize One Tap client
        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.google_client_id))  // Set your server client ID
                    .setFilterByAuthorizedAccounts(false)  // Allow both new and previously used accounts
                    .build()
            )
            .setAutoSelectEnabled(true)  // Automatically sign in if possible
            .build()
    }

    private fun performSignIn() {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(this) { result ->
                try {
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender, REQ_ONE_TAP, null, 0, 0, 0, null
                    )
                } catch (e: IntentSender.SendIntentException) {
                    // Handle exception
                    Toast.makeText(this, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener(this) { e ->
                // Handle failure
                Toast.makeText(this, "Sign-in failed: ${e.localizedMessage}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    if (idToken != null) {
                        // Handle the ID token (e.g., send it to your server)
                        Toast.makeText(this, "Sign-in successful!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "No ID token!", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: ApiException) {
                    // Handle error
                    Toast.makeText(this, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}