package com.example.myapplication.di

import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.features.main.MainActivity
import com.example.myapplication.features.main.MainActivityViewModel
import com.example.myapplication.repository.FortressRepository
import com.example.myapplication.utils.ActivityViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(ActivityComponent::class)
object MainActivityModule {


    @Provides
    fun bindMainActivityViewModel(@ActivityContext activity: MainActivity,
                                  repository: FortressRepository, coroutineContext: CoroutineContext): MainActivityViewModel{
        return ViewModelProvider(activity)
            .get(MainActivityViewModel::class.java)
    }

//    @Provides
//    fun provideFingerprintUtils(encryptionUtils: EncryptionUtils, activity: MainActivity)
//    = FingerprintUtils(encryptionUtils, activity)
}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher