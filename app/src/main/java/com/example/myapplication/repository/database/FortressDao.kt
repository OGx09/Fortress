package com.example.myapplication.repository.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FortressDao {

    @Query("SELECT * FROM passwordentity")
    suspend fun getAll(): List<PasswordEntity>

    @Query("SELECT * FROM passwordentity WHERE id = :id")
    suspend fun getPasswordDetails(id: Int): PasswordEntity


    @Query("SELECT encryptedData FROM passwordentity WHERE id = :id")
    suspend fun getEncryptedEntity(id: Int): String

    @Insert
    suspend fun insert(passwordEntity: PasswordEntity)

    @Insert
    suspend fun insertEncryptedEntity(passwordEntity: PasswordEntity)

    @Delete
    suspend fun delete(passwordEntity: PasswordEntity)
}