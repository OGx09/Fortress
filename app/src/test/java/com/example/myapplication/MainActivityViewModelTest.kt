package com.example.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.features.main.MainActivityViewModel
import com.example.myapplication.features.ui.UiState
import com.example.myapplication.repository.FortressRepository
import com.example.myapplication.repository.database.PasswordEntity
import kotlinx.coroutines.*
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

    @Mock
    lateinit var fortressRepository : RepositoryMock

    @Mock
    lateinit var mainViewModel: MainActivityViewModel

    @Captor
    lateinit var argumentCaptor : ArgumentCaptor<List<PasswordEntity>>

    @JvmField
    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()


    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    @Before
    fun init(){
        Dispatchers.setMain(mainThreadSurrogate)
        mainViewModel = MainActivityViewModel(coroutineContext = mainThreadSurrogate,
            repository = fortressRepository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `test save password entity`() = runBlockingTest{
        mainViewModel.checkForExistingLogin()
        assertNotNull(mainViewModel.openWelcomeOrPasswordMain)
        assertEquals(mainViewModel.openWelcomeOrPasswordMain.getAwaitValue(), UiState<String>(isLoading = true))
    }


    @ExperimentalCoroutinesApi
    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

}