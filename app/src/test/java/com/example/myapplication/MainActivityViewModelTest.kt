package com.example.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.runtime.collectAsState
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.myapplication.features.main.MainActivityViewModel
import com.example.myapplication.features.ui.UiState
import com.example.myapplication.repository.FortressRepository
import com.example.myapplication.repository.FortressRepositoryImpl
import com.example.myapplication.repository.database.PasswordEntity
import com.example.myapplication.utils.EncryptionUtils
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

// Created by Gbenga Oladipupo(Devmike01) on 7/31/21.

@RunWith(MockitoJUnitRunner::class)
class MainActivityViewModelTest {

    @Captor
    lateinit var argumentCaptor : ArgumentCaptor<List<PasswordEntity>>

    @JvmField
    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI NO thread")

    @ExperimentalCoroutinesApi
    private val mTestCoroutineDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()


    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    val mainViewModel: MainActivityViewModel = MainActivityViewModel(coroutineContext = mainThreadSurrogate,
    repository = RepositoryMock(mock(EncryptionUtils::class.java),
        mTestCoroutineDispatcher), encryptedUtils = mock(EncryptionUtils::class.java));


    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @Test
    fun `test livedata setup`(){
        assertNotNull(mainViewModel.passwordDetails)
        assertNotNull(mainViewModel.messageState)
        assertNotNull(mainViewModel.openPasswordDetails)
        assertNotNull(mainViewModel.openPasswordMain)
        assertNotNull(mainViewModel.openWelcomeOrPasswordMain)
        assertNotNull(mainViewModel.savePasswordDataLiveData)
        assertNotNull(mainViewModel.savePasswordEntityLiveData)
    }

    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    @Test
    fun `test checkForExistingLogin()`() = runBlocking{
        mainViewModel.checkForExistingLogin()
        assertNull(mainViewModel.openWelcomeOrPasswordMain.value)
    }


    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @After
    fun tearDown(){
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

}