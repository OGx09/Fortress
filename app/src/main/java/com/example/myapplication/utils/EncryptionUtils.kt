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


class EncryptionUtils @Inject constructor(private val dao: FortressDao) {


    private val SHARED_PREFERENCE_KEY_IV = "iv"

    companion object {
        const val KEY_NAME: String = "androiddebugkey"
        const val VALIDITY_DURATION_SECONDS = 5
        const val ALLOWED_AUTHENTICATORS = KeyProperties.KEY_ALGORITHM_AES
    }

    fun generateSecretKey() {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore"
        )
        keyGenerator.init(getKeyGenParameterSpec())
        keyGenerator.generateKey()
    }

    fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")

        // Before the keystore can be accessed, it must be loaded.
        keyStore.load(null)
        return keyStore.getKey(KEY_NAME, null) as SecretKey
    }

    fun getCipher(): Cipher {
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

   suspend fun encryptSecretInformation(cipher: Cipher, passwordEntity: PasswordEntity) {
        // Exceptions are unhandled for getCipher() and getSecretKey().

       val gson = Gson()

       try {
           val bytes = cipher.doFinal((gson.toJson(passwordEntity.fortressModel)).toByteArray())
           val encrypted = Base64.encodeToString(bytes, Base64.NO_WRAP)

            dao.insertEncryptedEntity(passwordEntity)

        } catch (e: InvalidKeyException) {
            Log.e("MY_APP_TAG", "Key is invalid.")
        } catch (e: UserNotAuthenticatedException) {
            Log.d("MY_APP_TAG", "The key's validity timed out.")
        }
    }




    suspend fun decryptSecretInformation(cipher: Cipher, id: Int) :FortressModel?{
        // Exceptions are unhandled for getCipher() and getSecretKey().
        var fortressModel: FortressModel? =null

        val encryptedStrinng = dao.getEncryptedEntity(id)

        try {

            val decodedBytes: ByteArray = Base64.decode(encryptedStrinng, Base64.NO_WRAP)
            val decryptedInfo: ByteArray = cipher.doFinal(decodedBytes)

            val string = String(decryptedInfo)

            Log.d("MY_APP_TAG", "ddecrypted information: $string ")


        } catch (e: InvalidKeyException) {
            Log.e("MY_APP_TAG", "Key is invalid.")
        } catch (e: UserNotAuthenticatedException) {
            Log.d("MY_APP_TAG", "The key's validity timed out.")
        }


        return fortressModel
    }



    val  getDao : FortressDao = dao

}

