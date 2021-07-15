package com.example.myapplication.repository

import com.example.myapplication.repository.database.PasswordEntity
import com.example.myapplication.repository.models.FortressModel
import com.example.myapplication.repository.models.GroInvestmentResponse
import javax.crypto.Cipher

interface FortressRepository {

    suspend fun fetchAllEncryptedPasswords(): List<PasswordEntity>

    suspend fun fetchPasswordDetails(cipher: Cipher, id: Int) : FortressModel?

    suspend fun removePassword(passwordEntity: PasswordEntity)

    suspend fun savePassword(cipher: Cipher, passwordEntity: PasswordEntity)

    suspend fun fetchwebsiteDetails() : GroInvestmentResponse

    //suspend fun fetchDecryptedPasswords(cipher: Cipher): List<FortressModel>
}