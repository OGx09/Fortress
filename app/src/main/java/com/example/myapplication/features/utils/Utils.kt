package com.example.myapplication.features.utils

import android.os.Handler
import android.os.Looper
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import com.example.myapplication.features.repository.database.PasswordEntity
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

    private val deferred = CompletableDeferred<Boolean>()

    init{

    }

    fun doEncrypt(isEncrypt: Boolean, passwordEntity: PasswordEntity){
        this.isEncrypt = isEncrypt
        this.passwordEntity = passwordEntity
    }

    private suspend fun init(plainPassword: String){
        biometricPrompt = BiometricPrompt(activity, Executors.newSingleThreadExecutor(),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(activity,
                        "Authentication error: $errString", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    encryptionUtils.generateSecretKey()

//                    CoroutineScope(Dispatchers.IO).launch {
//                        if (isEncrypt){
//                            passwordEntity?.apply { passwordEntity
//                                this.platformPassword?.apply {
////                                    encryptionUtils
////                                        .encryptSecretInformation(this,
////                                            passwordEntity)
//                                }
//                            }
//                        }else{
//                            do
//                           // encryptionUtils.decryptSecretInformation()
//                        }
//                    }


                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(activity,
                            "Authentication succeeded!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
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


    fun show(){
        biometricPrompt.authenticate(promptInfo)
    }

}