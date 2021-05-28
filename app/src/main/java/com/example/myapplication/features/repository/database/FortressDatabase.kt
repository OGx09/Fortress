package com.example.myapplication.features.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import dagger.hilt.android.qualifiers.ActivityContext

@Database(entities = [PasswordEntity::class], version = 1)
abstract class FortressDatabase : RoomDatabase() {
    abstract fun passwordDao(): FortressDao
}