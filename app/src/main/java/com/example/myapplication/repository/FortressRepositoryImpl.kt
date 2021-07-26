package com.example.myapplication.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import com.example.myapplication.repository.database.PasswordEntity
import com.example.myapplication.data.FortressModel
import com.example.myapplication.data.WebsiteLogo
import com.example.myapplication.utils.EncryptionUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.crypto.Cipher
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "username_store")

class FortressRepositoryImpl (private val encryptionUtils: EncryptionUtils,
                             private val websiteLogoService: WebsiteLogoService,
                             private val dispatcher: CoroutineDispatcher,
                             private val datastore: DataStore<Preferences>) : FortressRepository{

    companion object{
        val DATASTORE_USERNAME = stringPreferencesKey("com.example.myapplication.DATASTORE_USERNAME")
    }

    override fun fetchAllEncryptedPasswords(): LiveData<List<PasswordEntity>> {
        return encryptionUtils.getDao.getAllEncryptedPassword()
    }

    override suspend fun fetchPasswordDetails(cipher: Cipher, id: Int)
    : FortressModel? = withContext(Dispatchers.IO){
        encryptionUtils.decryptSecretInformation(cipher, id)
    }

    override suspend fun removePassword(passwordEntity: PasswordEntity) = encryptionUtils.getDao.delete(passwordEntity = passwordEntity)

    override suspend fun savePassword(cipher: Cipher, passwordEntity: PasswordEntity) =
        withContext(dispatcher){
        encryptionUtils.encryptSecretInformation(
            cipher = cipher,
            passwordEntity = passwordEntity
        )
    }

    override suspend fun fetchwebsiteIcon(websiteUrl: String): WebsiteLogo = withContext(dispatcher){
        websiteLogoService.getWebsiteLogo(websiteUrl = websiteUrl)
    }

    override suspend fun saveToDataStore(value: String) {
        datastore.edit {settings ->
            settings[DATASTORE_USERNAME] = value
        }
    }

    override suspend fun fetchUsername(): Flow<String?> = dataStoreValue(DATASTORE_USERNAME)

    @Suppress("Unchecked")
    fun<T> dataStoreValue(key : Preferences.Key<*>) : Flow<T?> = datastore.data.map { pref -> pref[key] } as Flow<T?>


}