package com.example.myapplication.utils

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.security.keystore.KeyGenParameterSpec
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
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.example.myapplication.features.managepassword.AddPasswordActivity
import com.example.myapplication.repository.models.FortressModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ActivityContext
import java.security.spec.ECGenParameterSpec
import javax.crypto.Cipher
import kotlin.coroutines.CoroutineContext


class MainThreadExecutor: Executor {
    private val handler = Handler(Looper.getMainLooper())

    override fun execute(command: Runnable) {
        handler.post(command)
    }

}

class FingerprintUtils @Inject constructor(private val encrptedUtils: EncryptionUtils){

    lateinit var executor: Executor
    lateinit var biometricPrompt: BiometricPrompt
    lateinit var promptInfo: BiometricPrompt.PromptInfo
    private var isEncrypt : Boolean = false
    private var passwordEntity: PasswordEntity? =null
    private var mToBeSignedMessage: String? = null
    private var mutableLiveAuthResult: MutableLiveData<FingerprintResult> = MutableLiveData()

    fun authenticate(activity: FragmentActivity,result: (FingerprintResult) -> Unit){
        if (canUseBiometric(activity)){
            var signature: Signature? = null

            try {
                mToBeSignedMessage = "$KEY_NAME:1234567"
                signature = initSignature(KEY_NAME)
                Log.d("fingerprintUtils", "___> $signature}")
            }catch (e: java.lang.Exception){
                throw RuntimeException()
            }
           // showBiometricPrompt(signature = signature)
        }else{
            result.invoke(FingerprintResult(errorString = DEFAULT_ERROR_MSG))
        }
    }

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

    fun register(activity: FragmentActivity):
            LiveData<FingerprintResult> {
        if (canUseBiometric(activity)){
            var cipher: Cipher? = null
            try {
                cipher = encrptedUtils.getCipher()
                val secretKey = encrptedUtils.getSecretKey()
                cipher.init(Cipher.ENCRYPT_MODE, secretKey)

            }catch (e: java.lang.Exception){
                throw RuntimeException()
            }
            showBiometricPrompt(activity = activity,cipher = cipher)
        }else{
            mutableLiveAuthResult.value = FingerprintResult(errorString = DEFAULT_ERROR_MSG)
        }
        return mutableLiveAuthResult
    }

    private fun showBiometricPrompt(activity: FragmentActivity, cipher: Cipher?){

        val biometricPrompt = BiometricPrompt(activity,
            getMainThreadExecutor,
            authenticationCallback)

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()

            //Show biometric prompt
        if (cipher != null){
            Log.i(TAG, "Show biometric prompt");
            biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
        }
    }

    @Throws(Exception::class)
    private fun initSignature(keyame: String): Signature?{
        val keyPair = getKeyPair(keyame)
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
            mutableLiveAuthResult.value =  FingerprintResult(errorString = errString.toString())
            super.onAuthenticationError(errorCode, errString)
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)

            if (result.cryptoObject != null){
                try{

                    mutableLiveAuthResult.value = FingerprintResult(cryptoObject = result.cryptoObject)
                }catch (e: SignatureException){
                    mutableLiveAuthResult.value = FingerprintResult(errorString = e.message)
                }
            }
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            mutableLiveAuthResult.value = FingerprintResult(errorString = DEFAULT_ERROR_MSG)
        }
    }


    fun dispose(observer: LifecycleOwner){
        mutableLiveAuthResult.removeObservers(observer)
    }

    companion object{
        private const val TAG = "FingerprintUtils"
        private val KEY_NAME = UUID.randomUUID().toString()
        private const val DEFAULT_ERROR_MSG ="Fingerprint authentication failed!"

        fun canUseBiometric(activity: FragmentActivity) = BiometricManager.from(activity).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS
    }

}