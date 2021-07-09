package com.example.myapplication.di

import android.content.Context
import androidx.room.Room
import com.example.myapplication.features.managepassword.AddPasswordActivity
import com.example.myapplication.repository.FortressRepository
import com.example.myapplication.repository.FortressRepositoryImpl
import com.example.myapplication.repository.database.FortressDatabase
import com.example.myapplication.utils.EncryptionUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideFortressDb(@ApplicationContext context: Context): FortressDatabase{
        return Room
            .databaseBuilder(context, FortressDatabase::class.java, "fortress-db")
            .fallbackToDestructiveMigration() // Change to migration later
            .build()
    }

    @Provides
    @Singleton
    fun provideEncryptionUtils(db: FortressDatabase) = EncryptionUtils(db.passwordDao())

    @Provides
    @Singleton
    fun provideFortressRepository(encryptionUtils: EncryptionUtils): FortressRepository{
        return FortressRepositoryImpl(encryptionUtils)
    }
}