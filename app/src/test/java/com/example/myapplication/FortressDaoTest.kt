package com.example.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myapplication.repository.database.PasswordEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

// Created by Gbenga Oladipupo(Devmike01) on 8/1/21.

@RunWith(JUnit4::class)
class FortressDaoTest {


    @ExperimentalCoroutinesApi
    private lateinit var mTestCoroutineDispatcher: TestCoroutineDispatcher


    @JvmField
    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()


    var fortressDatabase : FortressDaoMock? = null

    @ExperimentalCoroutinesApi
    @Before
    fun init(){
        mTestCoroutineDispatcher = TestCoroutineDispatcher()
        fortressDatabase = FortressDaoMock()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun savePasswordToDb() = mTestCoroutineDispatcher.runBlockingTest{
        val passwordEntity = PasswordEntity.getMock()
        fortressDatabase?.insert(passwordEntity)
        assertEquals(fortressDatabase?.getPasswordDetails(0)?.getAwaitValue(), "Google.com")
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown(){
        mTestCoroutineDispatcher.run {
            cleanupTestCoroutines()
        }
        fortressDatabase?.run {
            clear
        }
        fortressDatabase = null
    }
}