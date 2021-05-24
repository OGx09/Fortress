package com.example.myapplication.features.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PasswordEntity::class], version = 1)
abstract class FortressDatabase : RoomDatabase() {
    abstract fun passwordDao(): FortressDao
}