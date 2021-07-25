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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import java.security.spec.ECGenParameterSpec
import javax.crypto.Cipher


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

    val _mutableLiveAuthResultChannel: Channel<FingerprintResult> = Channel<FingerprintResult>(Channel.BUFFERED)
    val mutableLiveAuthResultFlow = _mutableLiveAuthResultChannel.receiveAsFlow()

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

    fun register(activity: FragmentActivity) {
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
            _mutableLiveAuthResultChannel.sendResult(FingerprintResult(errorString = DEFAULT_ERROR_MSG))
        }
    }


    private fun Channel<FingerprintResult>.sendResult(value: FingerprintResult){
        var job: Job? = null
        job = CoroutineScope(Dispatchers.Main).launch {
            Log.d("SavedPasswordItem", "hello : boo boo")
            _mutableLiveAuthResultChannel.trySend(value)
            if (job != null){
                _mutableLiveAuthResultChannel.cancel()
                job?.cancel()
                Log.d("SavedPasswordItem", "hello : boo cancel $_mutableLiveAuthResultChannel")
            }
        }.job

        Log.d("SavedPasswordItem", "hello : boo cancel ${job.isActive}")
//        if (job.isActive){
//            Log.d("SavedPasswordItem", "hello : boo isCompleted")
//            job.cancel()
//            _mutableLiveAuthResultChannel.close()
//        }
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
            _mutableLiveAuthResultChannel.sendResult(FingerprintResult(errorString = errString.toString()))
            super.onAuthenticationError(errorCode, errString)
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)

            if (result.cryptoObject != null){

                _mutableLiveAuthResultChannel.sendResult(FingerprintResult(cryptoObject = result.cryptoObject))
            }
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            _mutableLiveAuthResultChannel.sendResult(FingerprintResult(errorString = DEFAULT_ERROR_MSG))
        }
    }


    companion object{
        private const val TAG = "FingerprintUtils"
        private val KEY_NAME = UUID.randomUUID().toString()
        private const val DEFAULT_ERROR_MSG ="Fingerprint authentication failed!"

        fun canUseBiometric(activity: FragmentActivity) = BiometricManager.from(activity).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS
    }

}