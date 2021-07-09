package com.example.myapplication.di

import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.features.managepassword.AddPasswordActivity
import com.example.myapplication.features.managepassword.AddPaswordActivityViewModel
import com.example.myapplication.repository.FortressRepository
import com.example.myapplication.utils.ActivityViewModelFactory
import com.example.myapplication.utils.EncryptionUtils
import com.example.myapplication.utils.FingerprintUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@Module
@InstallIn(ActivityComponent::class)
class AddPasswordActivityModule {


//    @Provides
//    fun provideAddPasswordActivityViewModel(@ActivityContext activity: AddPasswordActivity,
//                                  repository: FortressRepository, fingerprintUtils: FingerprintUtils
//    ): AddPaswordActivityViewModel {
//        return ViewModelProvider(activity, ActivityViewModelFactory(repository = repository, fingerprintUtils))
//            .get(AddPaswordActivityViewModel::class.java)
//    }

    @Provides
    fun provideFingerprintUtils(encryptionUtils: EncryptionUtils,
                                @ActivityContext activity: AddPasswordActivity):FingerprintUtils{
        return FingerprintUtils(encryptionUtils, activity)
    }

}