package com.example.myapplication.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import com.example.myapplication.features.main.MainActivity
import com.example.myapplication.repository.database.PasswordEntity
import com.example.myapplication.repository.models.FortressModel
import com.example.myapplication.repository.models.WebsiteLogo
import com.example.myapplication.utils.EncryptionUtils
import javax.crypto.Cipher
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "username_store")

class FortressRepositoryImpl(private val encryptionUtils: EncryptionUtils,
                             private val websiteLogoService: WebsiteLogoService,
                             private val datastore: DataStore<Preferences>) : FortressRepository{

    companion object{
        val DATASTORE_USERNAME = stringPreferencesKey("com.example.myapplication.DATASTORE_USERNAME")
    }

    override fun fetchAllEncryptedPasswords(): LiveData<List<PasswordEntity>> {
        return encryptionUtils.getDao.getAllEncryptedPassword()
    }

    override suspend fun fetchPasswordDetails(cipher: Cipher, id: Int): FortressModel?{
        return encryptionUtils.decryptSecretInformation(cipher, id)
    }

    override suspend fun removePassword(passwordEntity: PasswordEntity) = encryptionUtils.getDao.delete(passwordEntity = passwordEntity)

    override suspend fun savePassword(cipher: Cipher, passwordEntity: PasswordEntity){
        encryptionUtils.encryptSecretInformation(cipher = cipher, passwordEntity = passwordEntity)
    }

    override suspend fun fetchwebsiteIcon(websiteUrl: String): WebsiteLogo {
        return websiteLogoService.getWebsiteLogo(websiteUrl = websiteUrl)
    }

    override suspend fun saveToDataStore(value: String) {
        datastore.edit {settings ->
            settings[DATASTORE_USERNAME] = value
        }
    }


//    override suspend fun fetchDecryptedPasswords(cipher: Cipher): List<FortressModel> {
//
//    }

}