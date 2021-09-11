package com.example.myapplication.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.security.keystore.UserNotAuthenticatedException
import android.util.Base64
import android.util.Log
import com.example.myapplication.repository.database.FortressDao
import com.example.myapplication.repository.database.PasswordEntity
import com.example.myapplication.data.FortressModel
import com.google.gson.Gson
import okio.Utf8
import java.lang.Byte.decode
import java.net.URLDecoder
import java.nio.charset.Charset
import java.security.InvalidKeyException
import java.security.KeyStore
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.inject.Inject
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.Base64.getDecoder
import javax.crypto.spec.SecretKeySpec
import kotlin.collections.HashMap


interface EncryptionUtils{
    fun getCipher(): Cipher
    fun decryptSecretInformation( cipherText: ByteArray, cipher: Cipher?) : String?
    fun encryptSecretInformation(passwordEntity: PasswordEntity, cipher: Cipher): CiphertextWrapper?
    fun getInitializedCipherForEncryption(keyName: String): Cipher
    suspend fun decryptDbCiperText(id: Int): FortressModel
    suspend fun persistInDbAsCipherText(passwordEntity: PasswordEntity)
    suspend fun getDao(): FortressDao
}

data class CiphertextWrapper(val ciphertext: ByteArray, val initializationVector: ByteArray)

class EncryptionUtilsImpl @Inject constructor(private val dao: FortressDao) : EncryptionUtils{

    private val token = "helloWorldOfDJjango"

    private val SHARED_PREFERENCE_KEY_IV = "iv"

    private val ENCRYPTION_BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
    private val ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
    private val ENCRYPTION_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
    private val KEY_SIZE = 256

    companion object {
        const val KEY_NAME: String = "androiddebugkey"
        const val VALIDITY_DURATION_SECONDS = 5
        const val ALLOWED_AUTHENTICATORS = KeyProperties.KEY_ALGORITHM_AES
    }

    //TODO: To be removed later
    override suspend fun getDao(): FortressDao = dao

    override fun getInitializedCipherForEncryption(keyName: String): Cipher {
        val cipher = getCipher()
        val secretKey = generateSecretKey(keyName = keyName);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
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


    override fun encryptSecretInformation(passwordEntity: PasswordEntity, cipher: Cipher): CiphertextWrapper? {
        // Exceptions are unhandled for getCipher() and getSecretKey().
       val gson = Gson()
        val encryptedText = cipher.doFinal(gson.toJson(passwordEntity.fortressModel).toByteArray(Charset.forName("UTF-8")))
        return CiphertextWrapper(encryptedText, cipher.iv)
    }

    override suspend fun decryptDbCiperText(id: Int): FortressModel {
        val json = dao.getEncryptedEntity(id)

        return dao.getPasswordDetails(id).let {
            Gson().fromJson(json, FortressModel::class.java)
        }

    }

    override suspend fun persistInDbAsCipherText(passwordEntity: PasswordEntity) {
        passwordEntity.run {
            dao.insertEncryptedEntity(this)
        }
    }

    override fun decryptSecretInformation(cipherText: ByteArray, cipher: Cipher?) :String?{
        // Exceptions are unhandled for getCipher() and getSecretKey().
         return cipher?.let {
             String(it.doFinal(cipherText), Charset.forName("UTF-8"))
        }
    }




}

