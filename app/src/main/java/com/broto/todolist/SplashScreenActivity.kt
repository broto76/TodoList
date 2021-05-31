package com.broto.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    private var mSnackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivSplashImage.animate()

        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            authenticateUser()
        }
    }

    private fun authenticateUser() {
        val executor = ContextCompat.getMainExecutor(this)
        mBiometricPrompt = BiometricPrompt(this, executor,
            object: BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    CoroutineScope(Dispatchers.Main).launch {
                        mSnackbar = Snackbar
                            .make(binding.root, "Failed To Verify", Snackbar.LENGTH_SHORT)
                        mSnackbar?.show()
                        delay(4000)
                        finish()
                    }
                }

                override fun onAuthenticationFailed() {
                    CoroutineScope(Dispatchers.Main).launch {
                        mSnackbar = Snackbar
                            .make(binding.root, "Failed To Verify", Snackbar.LENGTH_SHORT)
                        mSnackbar?.show()
                    }
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    if (mSnackbar?.isShown == true) {
                        mSnackbar?.dismiss()
                    }
                    startActivity(Intent(this@SplashScreenActivity,
                        MainActivity::class.java))
                    finish()
                }
        })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.splash_auth_dialog_title))
            .setDescription(getString(R.string.splash_auth_dialog_description))
            .setNegativeButtonText("Cancel")
            .build()

        mBiometricPrompt.authenticate(promptInfo)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}