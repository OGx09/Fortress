package com.example.myapplication.utils

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
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
    lateinit var biometricPrompt: BiometricPrompt
    lateinit var promptInfo: BiometricPrompt.PromptInfo
    private var isEncrypt : Boolean = false
    private var passwordEntity: PasswordEntity? =null
    private var mToBeSignedMessage: String? = null

    private val _mutableLiveAuthResultChannel: MutableSharedFlow<FingerprintResult> =
        MutableSharedFlow<FingerprintResult>(replay = 1, onBufferOverflow = BufferOverflow.DROP_LATEST)
    val mutableLiveAuthResultFlow = _mutableLiveAuthResultChannel.asSharedFlow()



    @Throws(Exception::class)
    private fun generateKeyPair(keyName: String, invalidateBiometricEnrollment: Boolean): KeyPair{
        val keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, "AndroidKeyStore")
        val keyBuilder = KeyGenParameterSpec.Builder(keyName, KeyProperties.PURPOSE_SIGN)
            .setAlgorithmParameterSpec(ECGenParameterSpec("secp256r1"))
            .setDigests(KeyProperties.DIGEST_SHA256,
                KeyProperties.DIGEST_SHA384, KeyProperties.DIGEST_SHA512)
            .setUserAuthenticationRequired(true)

        if(Build.VERSION.SDK_INT >= 24){
            keyBuilder.setInvalidatedByBiometricEnrollment(invalidateBiometricEnrollment)
        }
        keyPairGenerator.initialize(keyBuilder.build())


        return keyPairGenerator.generateKeyPair()
    }

    fun register(activity: FragmentActivity) {
        hasCalled = false
        if (canUseBiometric(activity)){
            showBiometricPrompt(activity = activity,
                cipher = encrptedUtils
                    .getInitializedCipherForEncryption(EncryptionUtilsImpl.KEY_NAME))
        }else{
            _mutableLiveAuthResultChannel.tryEmit(FingerprintResult(errorString = DEFAULT_ERROR_MSG))
        }
    }

    fun authenticate(activity: FragmentActivity, cipher: Cipher) {
        hasCalled = false
        if (canUseBiometric(activity)){
            showBiometricPrompt(activity = activity,
                cipher = cipher)
        }else{
            _mutableLiveAuthResultChannel.tryEmit(FingerprintResult(errorString = DEFAULT_ERROR_MSG))
        }
    }

    private fun showBiometricPrompt(activity: FragmentActivity, cipher: Cipher?){

        val biometricPrompt = BiometricPrompt(activity,
            MainThreadExecutor(),
            authenticationCallback)

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()

            //Show biometric prompt
        if (cipher != null){
            Log.i(TAG, "Show biometric prompt $cipher");
            biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
        }
    }


    private val authenticationCallback : BiometricPrompt.AuthenticationCallback = object : BiometricPrompt.AuthenticationCallback(){
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            Log.e(TAG, "Error code: " + errorCode + "error String: " + errString)
            _mutableLiveAuthResultChannel.tryEmit(FingerprintResult(errorString = errString.toString()))
            super.onAuthenticationError(errorCode, errString)
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)

            if (result.cryptoObject != null){
                _mutableLiveAuthResultChannel.tryEmit(FingerprintResult(cryptoObject = result.cryptoObject))
            }
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            _mutableLiveAuthResultChannel.tryEmit(FingerprintResult(errorString = DEFAULT_ERROR_MSG))
        }
    }


    companion object{
        private const val TAG = "FingerprintUtils"
        private val KEY_NAME = UUID.randomUUID().toString()
        private const val DEFAULT_ERROR_MSG ="Fingerprint authentication failed!"

        fun canUseBiometric(activity: FragmentActivity) = BiometricManager.from(activity).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS
    }

}