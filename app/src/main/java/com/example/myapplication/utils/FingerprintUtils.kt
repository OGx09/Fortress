package com.example.myapplication.utils

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.MainThread
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import com.example.myapplication.repository.database.PasswordEntity
import kotlinx.coroutines.*
import java.security.KeyPair
import java.security.KeyStore
import java.security.PrivateKey
import java.security.Signature
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject


class FingerprintUtils @Inject constructor(private val encryptionUtils: EncryptionUtils, private val activity: FragmentActivity){

    lateinit var executor: Executor
    lateinit var biometricPrompt: BiometricPrompt
    lateinit var promptInfo: BiometricPrompt.PromptInfo
    private var isEncrypt : Boolean = false
    private var passwordEntity: PasswordEntity? =null
    private val TAG = "FingerprintUtils"
    val KEY_NAME = UUID.randomUUID().toString()
    private var mToBeSignedMessage: String? = null

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
                    Handler(Looper.getMainLooper()).post {
                        deferred.complete(result)
                        Toast.makeText(activity,
                            "Authentication succeeded!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    deferred.completeExceptionally(Exception("Fingerprint authentication has failed!"))
                    Handler(Looper.getMainLooper()).run {
                        Toast.makeText(activity, "AuonAuthenticationSucceededthentication failed",
                            Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()
    }


    fun show(): CompletableDeferred<BiometricPrompt.AuthenticationResult>{
        var signature: Signature? = null

        try {
            mToBeSignedMessage = "$KEY_NAME:1234567"
            signature = initSignature()
        }catch (e: java.lang.Exception){
            e.printStackTrace()
        }
        biometricPrompt.authenticate(promptInfo)
        return deferred
    }


    @Throws(Exception::class)
    private fun initSignature(keyame: String): Signature?{
        val keyPair =getKeyPair(keyame)
        if (keyPair != null){
            val signature = Signature.getInstance("SHA256withECDSA")
            signature.initSign(keyPair.private)
            return signature
        }
        return null
    }

    val mainThreadLooper = MainThread

    @Throws(Exception::class)
    private fun getKeyPair(keyName: String): KeyPair?{
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        if(keyStore.containsAlias(keyName)){
            //Get Public Key
            val publicKey = keyStore.getCertificate(keyName).publicKey
            //Get private key
            val privateKey = keyStore.getKey(keyName, null) as PrivateKey
            return KeyPair(publicKey, privateKey)
        }
        return null
    }

    val authenticationCallback : BiometricPrompt.AuthenticationCallback = object : BiometricPrompt.AuthenticationCallback(){
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            Log.e(TAG, "Error code: " + errorCode + "error String: " + errString)
            super.onAuthenticationError(errorCode, errString)
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            if (result.cryptoObject != null && result.cryptoObject?.signature != null){
                result.cryptoObject?.signature != null
                try{
                    val signature = result.cryptoObject?.signature
                    signature.update()
                }
            }
        }
    }

}