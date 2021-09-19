package com.example.myapplication.utils

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import com.example.myapplication.repository.database.PasswordEntity
import kotlinx.coroutines.*
import java.lang.Runnable
import java.lang.RuntimeException
import java.security.*
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject
import androidx.biometric.BiometricManager
import com.example.myapplication.repository.database.CipherTextWrapper
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import java.security.spec.ECGenParameterSpec
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class MainThreadExecutor: Executor {
    private val handler = Handler(Looper.getMainLooper())

    override fun execute(command: Runnable) {
        handler.post(command)
    }

}

object Constants {
    // encryption/decryption
    var AES_KEY = "0366D8637F9C6B21"
    var IV_VECTOR = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
}

var hasCalled : Boolean = false

suspend fun SharedFlow<FingerprintResult>.collectData(method: suspend  (FingerprintResult) -> Unit){

    collect{
        if (!hasCalled){
            method.invoke(it)
            hasCalled = true
        }
    }
}

class FingerprintUtils @Inject constructor(private val encrptedUtils: EncryptionUtils){

    lateinit var executor: Executor
    //lateinit var biometricPrompt: BiometricPrompt
    lateinit var promptInfo: BiometricPrompt.PromptInfo
    private var isEncrypt : Boolean = false
    private var passwordEntity: PasswordEntity? =null
    private var mToBeSignedMessage: String? = null

    fun register(activity: FragmentActivity,
                 processResult: (FingerprintResult) -> Unit) {
        hasCalled = false

        kotlin.runCatching {
            val promptInfo = buildBiometricPromptInfo()
            if (canUseBiometric(activity)) {
                val biometricPrompt = buildBiometricPrompt(
                    activity = activity,
                    processResult = processResult
                )

                val cipher =
                    encrptedUtils.getInitializedCipherForEncryption(EncryptionUtilsImpl.KEY_NAME)
                //encrptedUtils.encryptSecretInformation(cipher, cipher)
                biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
            }
        }.recoverCatching {
            processResult.invoke(FingerprintResult(it.message))
        }
    }
//
    fun authenticate(initializationVector:  ByteArray, activity: FragmentActivity,
                             processResult: (FingerprintResult) -> Unit) {
        hasCalled = false
        //val decrypt:  CipherTextWrapper = encrptedUtils.decryptDbCiperText(id)
        val promptInfo = buildBiometricPromptInfo()
        val biometricPrompt = buildBiometricPrompt(activity = activity, processResult)
        val cipher = encrptedUtils.getInitializedCipherForDecryption(EncryptionUtilsImpl.KEY_NAME,
            initializationVector)
        biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
    }

    private fun buildBiometricPrompt(activity: FragmentActivity,
                                     processResult: (FingerprintResult) -> Unit): BiometricPrompt{

        return BiometricPrompt(activity,
            MainThreadExecutor(),
            object : BiometricPrompt.AuthenticationCallback(){
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    Log.e(TAG, "Error code: " + errorCode + "error String: " + errString)
                    processResult.invoke(FingerprintResult(errorString = errString.toString()))
                    super.onAuthenticationError(errorCode, errString)
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)

                    if (result.cryptoObject != null){
                        processResult(FingerprintResult(cryptoObject = result.cryptoObject))
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    processResult(FingerprintResult(errorString = DEFAULT_ERROR_MSG))
                }
            })

    }

    private fun buildBiometricPromptInfo() : BiometricPrompt.PromptInfo
    = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Biometric login for my app")
        .setSubtitle("Log in using your biometric credential")
        .setNegativeButtonText("Use account password")
        .build()


        //Show biometric prompt
//        if (cipher != null){
//            Log.i(TAG, "Show biometric prompt $cipher");
//            biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
//        }
//    }


    companion object{
        private const val TAG = "FingerprintUtils"
        private val KEY_NAME = UUID.randomUUID().toString()
        private const val DEFAULT_ERROR_MSG ="Fingerprint authentication failed!"

        fun canUseBiometric(activity: FragmentActivity) = BiometricManager.from(activity).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS
    }

}