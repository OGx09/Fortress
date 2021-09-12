package com.example.myapplication.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import com.example.myapplication.repository.database.CipherTextWrapper
import java.lang.Exception
import java.nio.charset.Charset
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.inject.Inject
import javax.crypto.spec.GCMParameterSpec


interface EncryptionUtils{
    fun getCipher(): Cipher
    fun decryptSecretInformation( cipherText: ByteArray, cipher: Cipher) : String?
    fun encryptSecretInformation(cypherText: ByteArray, cipher: Cipher): CipherTextWrapper?
    fun getInitializedCipherForEncryption(keyName: String): Cipher
    fun getInitializedCipherForDecryption(keyName: String,
                                          initializationVector: ByteArray): Cipher
}
//
//data class CipherTextWrapper(val ciphertext: ByteArray, val initializationVector: ByteArray, cipherTextWrapper: CipherTextWrapper)

class EncryptionUtilsImpl @Inject constructor() : EncryptionUtils{

    private val token = "helloWorldOfDJjango"

    private val SHARED_PREFERENCE_KEY_IV = "iv"

    private val ENCRYPTION_BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
    private val ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
    private val ENCRYPTION_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
    private val KEY_SIZE = 256

    companion object {
        const val KEY_NAME: String = "androiddebugkey"
        const val VALIDITY_DURATION_SECONDS = 5
        private val ANDROID_KEYSTORE = "AndroidKeyStore"
        const val ALLOWED_AUTHENTICATORS = KeyProperties.KEY_ALGORITHM_AES
    }


    override fun getInitializedCipherForEncryption(keyName: String): Cipher {
        val cipher = getCipher()
        val secretKey = getOrCreateSecretKey(keyName = keyName);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        return cipher
    }


    override fun getInitializedCipherForDecryption(
        keyName: String,
        initializationVector: ByteArray
    ): Cipher {
        val cipher = getCipher()
        try {
            val secretKey = getOrCreateSecretKey(keyName)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(128, initializationVector))
        }catch (e: Exception){
            e.printStackTrace()
            Log.e("getInitializedCipherForDecryption", "$e")
        }
        return cipher
    }

    private fun generateSecretKey(keyName: String): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore"
        )
        keyGenerator.init(getKeyGenParameterSpec(keyName))
        return keyGenerator.generateKey()
    }

    private fun getSecretKey(keyName: String): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")

        // Before the keystore can be accessed, it must be loaded.
        keyStore.load(null)
        return keyStore.getKey(keyName, null) as SecretKey
    }

    override fun getCipher(): Cipher {
        return Cipher.getInstance("$ENCRYPTION_ALGORITHM/$ENCRYPTION_BLOCK_MODE/$ENCRYPTION_PADDING")
    }


    private fun getOrCreateSecretKey(keyName: String): SecretKey {
        // If Secretkey was previously created for that keyName, then grab and return it.
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null) // Keystore must be loaded before it can be accessed
        keyStore.getKey(keyName, null)?.let { return it as SecretKey }

        // if you reach here, then a new SecretKey must be generated for that keyName
        val paramsBuilder = KeyGenParameterSpec.Builder(
            keyName,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
        paramsBuilder.apply {
            setBlockModes(ENCRYPTION_BLOCK_MODE)
            setEncryptionPaddings(ENCRYPTION_PADDING)
            setKeySize(KEY_SIZE)
            setUserAuthenticationRequired(true)
        }

        val keyGenParams = paramsBuilder.build()

        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEYSTORE
        )
        keyGenerator.init(keyGenParams)
        return keyGenerator.generateKey()
    }


    private fun getKeyGenParameterSpec(keyName: String): KeyGenParameterSpec {
        return KeyGenParameterSpec.Builder(
            keyName,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(KEY_SIZE)
            .setUserAuthenticationRequired(true)
            .build()
    }



    override fun decryptSecretInformation(cipherText: ByteArray, cipher: Cipher) :String?{
        // Exceptions are unhandled for getCipher() and getSecretKey().
         try {
             Log.d("decryptSecretInformation", "DECRYPT_CIPHER $cipher")
             return String(cipher.doFinal(cipherText), Charset.forName("UTF-8"))
         }catch (exception: Exception){
             throw Exception(exception.cause)
         }
    }

    override fun encryptSecretInformation(
        cipherText: ByteArray,
        cipher: Cipher
    ): CipherTextWrapper? {
        Log.d("eNcryptSecretInformation", "ENCRYPT_CIPHER ${cipher.iv}")

        val encryptedText = cipher.doFinal(cipherText)
        return CipherTextWrapper(encryptedText, cipher.iv)
    }


}

