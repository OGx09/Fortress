package com.example.myapplication.repository.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.repository.models.FortressModel

@Dao
interface FortressDao {

    @Query("SELECT * FROM passwordentity")
    fun getAllEncryptedPassword(): LiveData<List<PasswordEntity>>

    @Query("SELECT * FROM passwordentity WHERE id = :id")
    fun getPasswordDetails(id: Int): LiveData<PasswordEntity>


    @Query("SELECT encryptedData FROM passwordentity WHERE id = :id")
    suspend fun getEncryptedEntity(id: Int): String

    @Insert
    suspend fun insert(passwordEntity: PasswordEntity)

    @Insert
    suspend fun insertEncryptedEntity(passwordEntity: PasswordEntity)

    @Delete
    suspend fun delete(passwordEntity: PasswordEntity)
}