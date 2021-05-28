package com.example.myapplication.features.di

import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.features.main.MainActivity
import com.example.myapplication.features.main.MainActivityViewModel
import com.example.myapplication.features.repository.FortressRepository
import com.example.myapplication.features.utils.MainActivityViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
object MainActivityModule {

    @Provides
    fun bindMainActivityViewModel(@ActivityContext activity: MainActivity,
                                  repository: FortressRepository): MainActivityViewModel{
        return ViewModelProvider(activity, MainActivityViewModelFactory(repository = repository))
            .get(MainActivityViewModel::class.java)
    }
}