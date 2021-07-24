package com.example.myapplication.repository

import androidx.lifecycle.LiveData
import com.example.myapplication.repository.database.PasswordEntity
import com.example.myapplication.data.FortressModel
import com.example.myapplication.data.WebsiteLogo
import javax.crypto.Cipher

interface FortressRepository {

    fun fetchAllEncryptedPasswords(): LiveData<List<PasswordEntity>>

    suspend fun fetchPasswordDetails(cipher: Cipher, id: Int) : FortressModel?

    suspend fun removePassword(passwordEntity: PasswordEntity)

    suspend fun savePassword(cipher: Cipher, passwordEntity: PasswordEntity)

    suspend fun fetchwebsiteIcon(websiteUrl: String) : WebsiteLogo

    suspend fun saveToDataStore(value: String)

    //suspend fun fetchDecryptedPasswords(cipher: Cipher): List<FortressModel>
}