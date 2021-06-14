package com.example.myapplication.features.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.security.keystore.UserNotAuthenticatedException
import android.util.Log
import androidx.biometric.BiometricPrompt
import com.example.myapplication.features.repository.database.FortressDao
import com.example.myapplication.features.repository.database.FortressDatabase
import com.example.myapplication.features.repository.database.PasswordEntity
import com.example.myapplication.features.repository.models.FortressModel
import com.google.gson.Gson
import java.nio.charset.Charset
import java.security.InvalidKeyException
import java.security.KeyStore
import java.util.*
import java.util.concurrent.Executor
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.inject.Inject

class EncryptionUtils @Inject constructor(private val dao: FortressDao) {

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

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")

        // Before the keystore can be accessed, it must be loaded.
        keyStore.load(null)
        return keyStore.getKey(KEY_NAME, null) as SecretKey
    }

    private fun getCipher(): Cipher {
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

   suspend fun encryptSecretInformation( passwordEntity: PasswordEntity?) {
        // Exceptions are unhandled for getCipher() and getSecretKey().
        val cipher = getCipher()
        val secretKey = getSecretKey()
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            val encryptedInfo: ByteArray = cipher.doFinal(
                passwordEntity.toString().toByteArray(Charset.defaultCharset())
            )
            dao.insertEncryptedEntity(Arrays.toString(encryptedInfo))

            Log.d(
                "MY_APP_TAG", "Encrypted information: " +
                        Arrays.toString(encryptedInfo)
            )
        } catch (e: InvalidKeyException) {
            Log.e("MY_APP_TAG", "Key is invalid.")
        } catch (e: UserNotAuthenticatedException) {
            Log.d("MY_APP_TAG", "The key's validity timed out.")
        }
    }




    suspend fun decryptSecretInformation(id: Int) :FortressModel?{
        // Exceptions are unhandled for getCipher() and getSecretKey().
        val cipher = getCipher()
        val secretKey = getSecretKey()
        var fortressModel: FortressModel? =null
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey)
            val encryptedStrinng = dao.getEncryptedEntity(id)
            val decryptedString: ByteArray = cipher.doFinal(encryptedStrinng.toByteArray())
            val serializeString = Gson().toJson(decryptedString)
            fortressModel = Gson().fromJson(serializeString, FortressModel::class.java)
            Log.d(
                "MY_APP_TAG", "Encrypted information: " +
                        decryptedString
            )
        } catch (e: InvalidKeyException) {
            fortressModel = null
            Log.e("MY_APP_TAG", "Key is invalid.")
        } catch (e: UserNotAuthenticatedException) {
            fortressModel = null
            Log.d("MY_APP_TAG", "The key's validity timed out.")
        } finally {
            return fortressModel
        }
    }

    val  getDao : FortressDao = dao

}

