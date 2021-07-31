package com.example.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.features.main.MainActivityViewModel
import com.example.myapplication.features.ui.UiState
import com.example.myapplication.repository.FortressRepository
import com.example.myapplication.repository.database.PasswordEntity
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Matchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

// Created by Gbenga Oladipupo(Devmike01) on 7/31/21.

@RunWith(MockitoJUnitRunner::class)
class MainActivityViewModelTest {

    @Mock
    lateinit var fortressRepository : FortressRepository

    @Mock
    lateinit var mainViewModel: MainActivityViewModel

    @Captor
    lateinit var argumentCaptor : ArgumentCaptor<List<PasswordEntity>>

    @JvmField
    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init(){

    }

    @Test
    fun `test save password entity`(){
        mainViewModel.checkForExistingLogin()
        assertTrue(mainViewModel.openWelcomeOrPasswordMain.getAwaitValue().isLoading)
        assertEquals(mainViewModel.openWelcomeOrPasswordMain.getAwaitValue().error, null)
        assertEquals(mainViewModel.openWelcomeOrPasswordMain.getAwaitValue(), "")
    }


}