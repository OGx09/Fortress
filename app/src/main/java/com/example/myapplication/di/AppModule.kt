package com.example.myapplication.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.myapplication.repository.FortressRepository
import com.example.myapplication.repository.FortressRepositoryImpl
import com.example.myapplication.repository.WebApi
import com.example.myapplication.repository.WebsiteLogoService
import com.example.myapplication.repository.database.FortressDatabase
import com.example.myapplication.repository.models.WebsiteLogo
import com.example.myapplication.utils.EncryptionUtils
import com.example.myapplication.utils.dataStore
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import kotlin.properties.ReadOnlyProperty

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
    fun provideRetrofit(): WebsiteLogoService{
        val retrofit = Retrofit.Builder()
            .baseUrl(WebApi.BEST_ICON)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
        return retrofit.create(WebsiteLogoService::class.java)
    }

    @Provides
    @Singleton
    fun provideFortressRepository(encryptionUtils: EncryptionUtils,
                                  websiteLogoService: WebsiteLogoService,
                                  dataStore: DataStore<Preferences> ): FortressRepository{
        return FortressRepositoryImpl(encryptionUtils, websiteLogoService, dataStore)
    }

    @Provides
    @Singleton
    fun provideDatastore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }
}