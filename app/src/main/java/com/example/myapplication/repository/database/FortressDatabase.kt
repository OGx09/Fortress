package com.example.myapplication.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PasswordEntity::class], version = 1, exportSchema = false)
abstract class FortressDatabase : RoomDatabase() {
    abstract fun passwordDao(): FortressDao
}