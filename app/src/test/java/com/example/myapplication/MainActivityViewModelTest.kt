package com.example.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.features.main.MainActivityViewModel
import com.example.myapplication.features.ui.UiState
import com.example.myapplication.repository.FortressRepository
import com.example.myapplication.repository.database.PasswordEntity
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

// Created by Gbenga Oladipupo(Devmike01) on 7/31/21.

@RunWith(MockitoJUnitRunner::class)
class MainActivityViewModelTest {

    lateinit var fortressRepository : RepositoryMock

    @Mock
    lateinit var mainViewModel: MainActivityViewModel

    @Captor
    lateinit var argumentCaptor : ArgumentCaptor<List<PasswordEntity>>

    @JvmField
    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var dataStore: DataStore<Preferences>

    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @ExperimentalCoroutinesApi
    private val mTestCoroutineDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    @Before
    fun init(){
        Dispatchers.setMain(mainThreadSurrogate)
        val mockDatabase = FortressDaoMock()
        fortressRepository = RepositoryMock(EncryptionUtilsMock(mockDatabase), mTestCoroutineDispatcher, dataStore)

        mainViewModel = MainActivityViewModel(coroutineContext = mainThreadSurrogate,
            repository = fortressRepository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `test save password entity`(){
        mainViewModel.checkForExistingLogin()
        assertNotNull(fortressRepository)
        assertNotNull(mainViewModel.openWelcomeOrPasswordMain)
        assertEquals(mainViewModel.openWelcomeOrPasswordMain.getAwaitValue(), UiState<String>(isLoading = true))
        assertEquals(mainViewModel.openWelcomeOrPasswordMain.getAwaitValue(), UiState<String>(isLoading = false))
    }


    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @After
    fun tearDown(){
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

}