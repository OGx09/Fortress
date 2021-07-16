package com.example.myapplication.repository

import androidx.lifecycle.LiveData
import com.example.myapplication.repository.database.PasswordEntity
import com.example.myapplication.repository.models.FortressModel
import com.example.myapplication.repository.models.WebsiteLogo
import com.example.myapplication.utils.EncryptionUtils
import javax.crypto.Cipher

class FortressRepositoryImpl(private val encryptionUtils: EncryptionUtils, private val websiteLogoService: WebsiteLogoService) : FortressRepository{

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

//    override suspend fun fetchDecryptedPasswords(cipher: Cipher): List<FortressModel> {
//
//    }

}