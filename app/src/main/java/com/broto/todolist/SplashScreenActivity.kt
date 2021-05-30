package com.broto.todolist


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.broto.todolist.databinding.ActivitySplashScreenBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {

    private val TAG = "SplashScreenActivity"
    private var mFailedAttempt = 0

    private var _binding: ActivitySplashScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var mBiometricPrompt: BiometricPrompt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CoroutineScope(Dispatchers.Main).launch {
            delay(1500)
            authenticateUser()
        }
    }

    private fun authenticateUser() {
        val executor = ContextCompat.getMainExecutor(this)
        mBiometricPrompt = BiometricPrompt(this, executor,
            object: BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    CoroutineScope(Dispatchers.Main).launch {
                        Snackbar
                            .make(binding.root, "Failed To Verify", Snackbar.LENGTH_SHORT)
                            .show()
                        delay(4000)
                        finish()
                    }
                }

                override fun onAuthenticationFailed() {
                    CoroutineScope(Dispatchers.Main).launch {
                        Snackbar
                            .make(binding.root, "Failed To Verify", Snackbar.LENGTH_SHORT)
                            .show()
                        delay(4000)
                        finish()
                    }
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    startActivity(Intent(this@SplashScreenActivity,
                        MainActivity::class.java))
                    finish()
                }
        })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Waiting for Authentication")
            .setDescription("Place your finger over the fingerprint scanner")
            .setNegativeButtonText("Cancel")
            .build()

        mBiometricPrompt.authenticate(promptInfo)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}