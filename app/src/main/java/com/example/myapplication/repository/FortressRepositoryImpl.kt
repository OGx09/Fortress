package com.example.myapplication.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import com.example.myapplication.data.SecretDataWrapper
import com.example.myapplication.repository.database.PasswordEntity
import com.example.myapplication.data.WebsiteLogo
import com.example.myapplication.repository.database.CipherTextWrapper
import com.example.myapplication.repository.database.FortressDao
import com.example.myapplication.utils.DragonLog
import com.example.myapplication.utils.DragonLog.spit
import com.example.myapplication.utils.EncryptionUtils
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.nio.charset.Charset
import javax.crypto.Cipher

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "username_store")


interface FortressRepository {

    fun fetchAllEncryptedPasswords(): LiveData<List<PasswordEntity>>

    suspend fun fetchPasswordDetails(id: Int, cipher: Cipher) : PasswordEntity?

    suspend fun removePassword(passwordEntity: PasswordEntity)

    suspend fun savePassword(cipher: Cipher, passwordEntity: PasswordEntity)

    suspend fun fetchwebsiteIcon(websiteUrl: String) : WebsiteLogo

    suspend fun saveToDataStore(value: String)

    suspend fun getCipherTextFromDb(id: Int): ByteArray?

    suspend fun fetchUsername(): Flow<String?>
    suspend fun decryptDbCiperText(id: Int): CipherTextWrapper
    suspend fun persistInDbAsCipherText(passwordEntity: PasswordEntity)

    //suspend fun fetchDecryptedPasswords(cipher: Cipher): List<FortressModel>
}

class FortressRepositoryImpl (private val websiteLogoService: WebsiteLogoService,
                              private val encryptionUtils: EncryptionUtils,
                             private val dispatcher: CoroutineDispatcher,
                              private val dao : FortressDao,
                             private val datastore: DataStore<Preferences>) : FortressRepository{

    companion object{
        private const val TAG = "FortressRepositoryImpl"
        val DATASTORE_USERNAME = stringPreferencesKey("com.example.myapplication.DATASTORE_USERNAME")
    }

    override fun fetchAllEncryptedPasswords(): LiveData<List<PasswordEntity>> {
        return dao.getAllEncryptedPassword()
    }

    override suspend fun fetchPasswordDetails(id: Int, cipher: Cipher)
    : PasswordEntity? = withContext(Dispatchers.IO){
        dao.getPasswordDetails(id).let {passwordEntity ->
            Gson().run {
                val encryptedData = fromJson(passwordEntity?.encryptedData, ByteArray::class.java)
                val decryptedString = encryptionUtils.decryptSecretInformation(encryptedData, cipher)
                fromJson(decryptedString, SecretDataWrapper::class.java).let {
                    passwordEntity?.encryptedData = null
                    passwordEntity?.secretDataWrapper = it
                    passwordEntity
                }
            }
        }

        // Gson().fromJson(decryptedString, PasswordEnt[ity::class.java)
    }

    override suspend fun removePassword(passwordEntity: PasswordEntity) = dao.delete(passwordEntity = passwordEntity)

    override suspend fun savePassword(cipher: Cipher, passwordEntity: PasswordEntity){
        DragonLog.spit("savePassword", "$passwordEntity")
        withContext(dispatcher){
            dao.insert(passwordEntity)
        }
    }

    override suspend fun fetchwebsiteIcon(websiteUrl: String): WebsiteLogo = withContext(dispatcher){
        websiteLogoService.getWebsiteLogo(websiteUrl = websiteUrl)
    }

    override suspend fun saveToDataStore(value: String) {
        datastore.edit {settings ->
            settings[DATASTORE_USERNAME] = value
        }
    }

    override suspend fun getCipherTextFromDb(id: Int): ByteArray? {
        val encryptedEntity = dao.getEncryptedEntity(id)
        return encryptedEntity?.toByteArray(Charset.forName("UTF-8"))
    }

    override suspend fun fetchUsername(): Flow<String?> = dataStoreValue(DATASTORE_USERNAME)

    @Suppress("Unchecked")
    fun<T> dataStoreValue(key : Preferences.Key<*>) : Flow<T?> = datastore.data.map { pref -> pref[key] } as Flow<T?>

    override suspend fun decryptDbCiperText(id: Int): CipherTextWrapper {
        return dao.getPasswordDetails(id).let {
            Gson().fromJson(it?.encryptedData, CipherTextWrapper::class.java)
        }

    }

    override suspend fun persistInDbAsCipherText(passwordEntity: PasswordEntity) {
        passwordEntity.run {
            dao.insert(this)
        }
    }

}