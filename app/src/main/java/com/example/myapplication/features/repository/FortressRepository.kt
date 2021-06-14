package com.example.myapplication.features.repository

import com.example.myapplication.features.repository.database.FortressDao
import com.example.myapplication.features.repository.database.PasswordEntity
import com.example.myapplication.features.repository.models.FortressModel

interface FortressRepository {

    suspend fun fetchAllPasswords(): List<PasswordEntity>

    suspend fun fetchPasswordDetails(id: Int) : FortressModel?

    suspend fun removePassword(passwordEntity: PasswordEntity)

    suspend fun savePassword(passwordEntity: FortressModel)
}