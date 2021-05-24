package com.example.myapplication.features.repository

import com.example.myapplication.features.repository.database.FortressDao
import com.example.myapplication.features.repository.database.PasswordEntity

class FortressRepositoryImpl(private val dao: FortressDao) : FortressRepository{

    override suspend fun fetchAllPasswords(): List<PasswordEntity> {
        return dao.getAll()
    }

    override suspend fun fetchPasswordDetails(id: Int): PasswordEntity = dao.getPasswordDetails(id)

    override suspend fun removePassword(passwordEntity: PasswordEntity) = dao.delete(passwordEntity = passwordEntity)

    override suspend fun savePassword(passwordEntity: PasswordEntity) = dao.insert(passwordEntity = passwordEntity)

}