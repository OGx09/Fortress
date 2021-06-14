package com.example.myapplication.features.di

import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.features.managepassword.AddPasswordActivity
import com.example.myapplication.features.managepassword.AddPaswordActivityViewModel
import com.example.myapplication.features.repository.FortressRepository
import com.example.myapplication.features.utils.ActivityViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext


@Module
@InstallIn(ActivityComponent::class)
class AddPasswordActivityModule {

    @Provides
    fun provideAddPasswordActivityViewModel(@ActivityContext activity: AddPasswordActivity,
                                  repository: FortressRepository
    ): AddPaswordActivityViewModel {
        return ViewModelProvider(activity, ActivityViewModelFactory(repository = repository))
            .get(AddPaswordActivityViewModel::class.java)
    }

}