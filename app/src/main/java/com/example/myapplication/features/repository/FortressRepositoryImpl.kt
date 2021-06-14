package com.example.myapplication.features.repository

import com.example.myapplication.features.repository.database.PasswordEntity
import com.example.myapplication.features.repository.models.FortressModel
import com.example.myapplication.features.utils.EncryptionUtils

class FortressRepositoryImpl(private val encryptionUtils: EncryptionUtils) : FortressRepository{

    override suspend fun fetchAllPasswords(): List<PasswordEntity> {
        return encryptionUtils.getDao.getAll()
    }

    override suspend fun fetchPasswordDetails(id: Int): FortressModel?{
        return encryptionUtils.decryptSecretInformation(id)
    }

    override suspend fun removePassword(passwordEntity: PasswordEntity) = encryptionUtils.getDao.delete(passwordEntity = passwordEntity)

    override suspend fun savePassword(passwordEntity: FortressModel){
        encryptionUtils.encryptSecretInformation(passwordEntity = passwordEntity)
    }

}