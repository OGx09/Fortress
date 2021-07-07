package com.example.myapplication.utils

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import com.example.myapplication.repository.database.PasswordEntity
import kotlinx.coroutines.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject


class FingerprintUtils @Inject constructor(private val encryptionUtils: EncryptionUtils, private val activity: FragmentActivity){

    lateinit var executor: Executor
    lateinit var biometricPrompt: BiometricPrompt
    lateinit var promptInfo: BiometricPrompt.PromptInfo
    private var isEncrypt : Boolean = false
    private var passwordEntity: PasswordEntity? =null

    private val deferred = CompletableDeferred<BiometricPrompt.AuthenticationResult>()

    init{
        init()
    }

    private fun init(){
        biometricPrompt = BiometricPrompt(activity, Executors.newSingleThreadExecutor(),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    deferred.completeExceptionally(Exception("$errString"))
                    Toast.makeText(activity,
                        "Authentication error: $errString", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    encryptionUtils.generateSecretKey()
                    deferred.complete(result)
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(activity,
                            "Authentication succeeded!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    deferred.completeExceptionally(Exception("Fingerprint authentication has failed!"))
                    Toast.makeText(activity, "AuonAuthenticationSucceededthentication failed",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()
    }


    fun show(): CompletableDeferred<BiometricPrompt.AuthenticationResult>{
        biometricPrompt.authenticate(promptInfo,
            BiometricPrompt.CryptoObject(encryptionUtils.getCipher()))
        return deferred
    }

}