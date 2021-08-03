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
import java.util.Base64.getDecoder


interface EncryptionUtils{
    fun generateSecretKey()
    fun getSecretKey(): SecretKey
    fun getCipher(): Cipher
    suspend fun decryptSecretInformation(cipher: Cipher?, id: Int) :FortressModel?
    suspend fun encryptSecretInformation(cipher: Cipher, passwordEntity: PasswordEntity)
    fun getDao(): FortressDao
}

class EncryptionUtilsImpl @Inject constructor(private val dao: FortressDao) : EncryptionUtils{


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


    private fun getKeyGenParameterSpec(): KeyGenParameterSpec {
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

       val gson = Gson()

       try {
           val jsonText = gson.toJson(passwordEntity.fortressModel)
           val encryptedInfo: ByteArray = cipher.doFinal(
               "Some Java World".toByteArray(Charset.defaultCharset())
           )

           val encryptedByte = java.util.Base64.getEncoder().encode(encryptedInfo)
           val encryptedString = java.util.Base64.getEncoder().encodeToString(encryptedByte)
           passwordEntity.encryptedData = encryptedString
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
                val decryptedInfo: ByteArray = this.doFinal(encryptedString.toByteArray(Charset.defaultCharset()))
                val text = String(decryptedInfo, Charsets.UTF_8)
                val text1 = Arrays.toString(decryptedInfo)

                val bytes = "Techie Delight".toByteArray(StandardCharsets.UTF_8)
                val string = String(bytes, StandardCharsets.UTF_8)
                Log.d("MY_APP_TAG", "DECODED =>$text1\n $text __ $encryptedString \n $string")

//                val string = String(decryptedInfo)
//                Log.d("MY_APP_TAG", "ddecrypted information: $decodedBytes ")
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

