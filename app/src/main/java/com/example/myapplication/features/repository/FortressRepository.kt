package com.example.myapplication.features.repository

import com.example.myapplication.features.repository.database.FortressDao
import com.example.myapplication.features.repository.database.PasswordEntity

interface FortressRepository {

    suspend fun fetchAllPasswords(): List<PasswordEntity>

    suspend fun fetchPasswordDetails(id: Int) : PasswordEntity

    suspend fun removePassword(passwordEntity: PasswordEntity)

    suspend fun savePassword(passwordEntity: PasswordEntity)
}