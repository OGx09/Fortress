package com.example.myapplication.features.di

import android.content.Context
import androidx.room.Room
import com.example.myapplication.features.repository.FortressRepository
import com.example.myapplication.features.repository.FortressRepositoryImpl
import com.example.myapplication.features.repository.database.FortressDatabase
import dagger.Binds
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
    fun provideFortressRepository(db: FortressDatabase): FortressRepository{
        return FortressRepositoryImpl(db.passwordDao())
    }


    //@Provides
    //fun provideEncryptionUtils() = EncryptionUtils()


}