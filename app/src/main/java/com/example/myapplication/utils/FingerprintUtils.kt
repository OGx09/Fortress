package com.example.myapplication.utils

import android.os.Handler
import android.os.Looper
import android.security.keystore.KeyProperties
import android.util.Base64.encodeToString
import android.util.Log
import android.widget.Toast
import androidx.annotation.MainThread
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import com.example.myapplication.repository.database.PasswordEntity
import kotlinx.coroutines.*
import java.lang.Runnable
import java.lang.RuntimeException
import java.security.*
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject
import android.util.Base64
import androidx.biometric.BiometricManager


class MainThreadExecutor: Executor {
    private val handler = Handler(Looper.getMainLooper())

    override fun execute(command: Runnable) {
        handler.post(command)
    }

}



class FingerprintUtils @Inject constructor(private val activity: FragmentActivity){

    lateinit var executor: Executor
    lateinit var biometricPrompt: BiometricPrompt
    lateinit var promptInfo: BiometricPrompt.PromptInfo
    private var isEncrypt : Boolean = false
    private var passwordEntity: PasswordEntity? =null
    private var mToBeSignedMessage: String? = null
    private lateinit var result: (FingerprintResult) -> Unit


    fun authenticate(result: (FingerprintResult) -> Unit){
        this.result = result
        if (canUseBiometric(activity)){
            var signature: Signature? = null

            try {
                mToBeSignedMessage = "$KEY_NAME:1234567"
                signature = initSignature(KEY_NAME)
            }catch (e: java.lang.Exception){
                throw RuntimeException()
            }
            showBiometricPrompt(signature = signature)
        }else{
            result.invoke(FingerprintResult(errorString = DEFAULT_ERROR_MSG))
        }
    }

    @Throws(Exception::class)
    private fun generateKeyPair(keyName: String, invalidateBiometricEnrollment: Boolean){
        val keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, "AndroidKeyStore")

    }

    fun register(registerFingerResult: (FingerprintResult) -> Unit){
        this.result = registerFingerResult
        if (canUseBiometric(activity)){
            var signature: Signature? = null

            try {
                mToBeSignedMessage = "$KEY_NAME:1234567"
                signature = initSignature(KEY_NAME)
            }catch (e: java.lang.Exception){
                throw RuntimeException()
            }
            showBiometricPrompt(signature = signature)
        }else{
            result.invoke(FingerprintResult(errorString = DEFAULT_ERROR_MSG))
        }
    }

    private fun showBiometricPrompt(signature: Signature?){

        val biometricPrompt = BiometricPrompt(activity, getMainThreadExecutor, authenticationCallback)

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()

            //Show biometric prompt
        if (signature != null){
            Log.i(TAG, "Show biometric prompt");
            biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(signature))
        }
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

    private val getMainThreadExecutor get() = MainThreadExecutor()


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

    private val authenticationCallback : BiometricPrompt.AuthenticationCallback = object : BiometricPrompt.AuthenticationCallback(){
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            Log.e(TAG, "Error code: " + errorCode + "error String: " + errString)
            this@FingerprintUtils.result.invoke(FingerprintResult(errorString = errString.toString()))
            super.onAuthenticationError(errorCode, errString)
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            if (result.cryptoObject != null && result.cryptoObject?.signature != null){
                result.cryptoObject?.signature != null
                try{
                    val signature = result.cryptoObject?.signature
                    signature?.update(mToBeSignedMessage?.toByteArray())
                    val signatureString = encodeToString(signature?.sign(), Base64.URL_SAFE)
                    //Signed message and signature are sent to server
                    Log.i(TAG, "Message: $mToBeSignedMessage");
                    Log.i(TAG, "Signature (Base64 Encoded): $signatureString");
                    this@FingerprintUtils.result.invoke(FingerprintResult(result = result))

                }catch (e: SignatureException){
                    this@FingerprintUtils.result.invoke(FingerprintResult(errorString = e.message))
                }
            }
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            this@FingerprintUtils.result.invoke(FingerprintResult(errorString = DEFAULT_ERROR_MSG))
        }
    }

    companion object{
        private const val TAG = "FingerprintUtils"
        private val KEY_NAME = UUID.randomUUID().toString()
        private const val DEFAULT_ERROR_MSG ="Fingerprint authentication failed!"

        fun canUseBiometric(activity: FragmentActivity) = BiometricManager.from(activity).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS
    }

}