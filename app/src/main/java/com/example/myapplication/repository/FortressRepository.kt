package com.example.myapplication.repository

import com.example.myapplication.repository.database.PasswordEntity
import com.example.myapplication.repository.models.FortressModel
import javax.crypto.Cipher

interface FortressRepository {

    suspend fun fetchAllPasswords(): List<PasswordEntity>

    suspend fun fetchPasswordDetails(id: Int) : FortressModel?

    suspend fun removePassword(passwordEntity: PasswordEntity)

    suspend fun savePassword(cipher: Cipher, passwordEntity: FortressModel)
}