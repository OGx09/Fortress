package com.example.myapplication.features.repository.database

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

    @Insert
    suspend fun insert(passwordEntity: PasswordEntity)

    @Delete
    suspend fun delete(passwordEntity: PasswordEntity)
}