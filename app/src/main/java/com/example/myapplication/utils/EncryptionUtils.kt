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


interface EncryptionUtils{
    fun generateSecretKey()
    fun getSecretKey(): SecretKey
    fun getCipher(): Cipher
    fun getKeyGenParameterSpec(): KeyGenParameterSpec
    suspend fun decryptSecretInformation(cipher: Cipher?, id: Int) :FortressModel?
    suspend fun encryptSecretInformation(cipher: Cipher, passwordEntity: PasswordEntity)
    fun getDao(): FortressDao
}

class EncryptionUtilsImpl @Inject constructor(private val dao: FortressDao) : EncryptionUtils{

    private val token = "helloWorldOfDJjango"

    private val SHARED_PREFERENCE_KEY_IV = "iv"

    companion object {
        const val KEY_NAME: String = "androiddebugkey"
        const val VALIDITY_DURATION_SECONDS = 5
        const val ALLOWED_AUTHENTICATORS = KeyProperties.KEY_ALGORITHM_AES
    }

    override fun generateSecretKey() {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore"
        )
        keyGenerator.init(getKeyGenParameterSpec())
        keyGenerator.generateKey()
    }

    override fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")

        // Before the keystore can be accessed, it must be loaded.
        keyStore.load(null)
        return keyStore.getKey(KEY_NAME, null) as SecretKey
    }

    override fun getCipher(): Cipher {
        return Cipher.getInstance(
            KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7
        )
    }


    override fun getKeyGenParameterSpec(): KeyGenParameterSpec {
        return KeyGenParameterSpec.Builder(
            KEY_NAME,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .setUserAuthenticationRequired(true)
            .build()
    }

    fun encrypt(plainText: ByteArray, key: SecretKey, iv: ByteArray): ByteArray{
        val _cipher = Cipher.getInstance("AES")
        val secretKeySpec = SecretKeySpec(key.encoded, "AES")
        val ivSpec = IvParameterSpec(iv)
        _cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec)
        val cipherText = _cipher.doFinal(plainText)
        return plainText
    }

    fun decrypt(key: SecretKey, cipherText: ByteArray, iv: ByteArray): String{
        try {
            val cipher = Cipher.getInstance("AES")
            val keySpec = SecretKeySpec(key.encoded, "AES")
            val ivSpec = IvParameterSpec(iv)
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
            val decryptedText = cipher.doFinal(cipherText)
            return String(decryptedText)
        }catch (e: Exception){

        }
        return ""
    }



    @Throws(java.lang.Exception::class)
    fun encrypt(message: String): String {
        val md = MessageDigest.getInstance("SHA-1")
        val digestOfPassword = md.digest(
            token.toByteArray()
        )
        val keyBytes: ByteArray = Arrays.copyOf(digestOfPassword, 24)
        var j = 0
        var k = 16
        while (j < 8) {
            keyBytes[k++] = keyBytes[j++]
        }
        val key: SecretKey = SecretKeySpec(keyBytes, "DESede")
        val s1 = "12345678"
        val bytes = s1.toByteArray()
        val iv = IvParameterSpec(bytes)
        val cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, key, iv)
        val plainTextBytes = message.toByteArray(charset("utf-8"))
        val cipherText = cipher.doFinal(plainTextBytes)
        val ss: String = base64encoding(cipherText, 0)
        val result: ByteArray = base64decodingByte(cipherText, 0)
        return String(result, StandardCharsets.UTF_8)
    }

    fun base64decodingByte(context: ByteArray?, type: Int): ByteArray {
        val result: ByteArray = Base64.encode(context, type)
        return result
    }

    private fun base64encoding(context: ByteArray?, type: Int): String {
        var result: String? = ""
        result = Base64.encodeToString(context, type)
        return result
    }

    @Throws(java.lang.Exception::class)
    fun decrypt(message: ByteArray?): String {
        val values: ByteArray = base64decodingByte(message, 0)
        val md = MessageDigest.getInstance("SHA-1")
        val digestOfPassword = md.digest(
            token
                .toByteArray(Charset.defaultCharset())
        )
        val keyBytes: ByteArray = Arrays.copyOf(digestOfPassword, 24)
        var j = 0
        var k = 16
        while (j < 8) {
            keyBytes[k++] = keyBytes[j++]
        }
        val key: SecretKey = SecretKeySpec(keyBytes, "DESede")
        val s1 = "12345678"
        val bytes = s1.toByteArray()
        val iv = IvParameterSpec(bytes)
        val decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding")
        decipher.init(Cipher.DECRYPT_MODE, key, iv)
        val plainText = decipher.doFinal(values)
        return String(plainText, StandardCharsets.UTF_8)
    }



    override suspend fun encryptSecretInformation(cipher: Cipher, passwordEntity: PasswordEntity) {
        // Exceptions are unhandled for getCipher() and getSecretKey().
        Log.d("decryptSecretInfir", "$cipher")

       val gson = Gson()

       try {
           val jsonText = gson.toJson(passwordEntity.fortressModel)

           Log.d("decryptSecretInfir", "$jsonText")

           val encryptedText = cipher.doFinal(jsonText.toByteArray())
           passwordEntity.encryptedData = Base64.encodeToString(encryptedText, Base64.NO_WRAP or Base64.DEFAULT) //String(encryptedInfo)
           dao.insertEncryptedEntity(passwordEntity)

        } catch (e: InvalidKeyException) {
            Log.e("MY_APP_TAG", "Key is invalid.")
        } catch (e: UserNotAuthenticatedException) {
            Log.d("MY_APP_TAG", "The key's validity timed out.")
        }
    }

    override fun getDao(): FortressDao = dao


    override suspend fun decryptSecretInformation(cipher: Cipher?, id: Int) :FortressModel?{
        // Exceptions are unhandled for getCipher() and getSecretKey().
        var fortressModel: FortressModel? =null

        val encryptedString = dao.getEncryptedEntity(id)
        cipher?.run {

            try {
               // val decryptedInfo: ByteArray = this.doFinal(encryptedString.toByteArray(Charset.defaultCharset()))
                val text = String(doFinal(Base64.decode(encryptedString.toByteArray(), Base64.NO_WRAP or Base64.DEFAULT)))
                Log.d("Hallelujjah!", "$text")
                fortressModel =  Gson().fromJson(text, FortressModel::class.java)

            } catch (e: InvalidKeyException) {
                Log.e("MY_APP_TAG", "Key is invalid.")
            } catch (e: UserNotAuthenticatedException) {
                Log.d("MY_APP_TAG", "The key's validity timed out.")
            }
        }


        return fortressModel
    }



    private fun decryptSecretInformation(data: String) {
        // Exceptions are unhandled for getCipher() and getSecretKey().
        val cipher = getCipher()
        val secretKey = getSecretKey()
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey)
            val encryptedInfo: ByteArray = cipher.doFinal(
                "Hello world of django".toByteArray(Charset.defaultCharset())
            )
            Log.d(
                "MY_APP_TAG212", "Encrypted information: " +
                        Arrays.toString(encryptedInfo)
            )
        } catch (e: InvalidKeyException) {
            Log.e("MY_APP_TAG", "Key is invalid.")
        } catch (e: UserNotAuthenticatedException) {
            Log.d("MY_APP_TAG", "The key's validity timed out.")
           // biometricPrompt.authenticate(promptInfo)
        }
    }

}

