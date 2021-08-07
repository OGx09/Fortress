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




}

