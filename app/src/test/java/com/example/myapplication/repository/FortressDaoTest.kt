package com.example.myapplication.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myapplication.FortressDaoMock
import com.example.myapplication.repository.database.PasswordEntity
import com.example.myapplication.utils.testhelper.TestConstants
import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class FortressDaoTest {

    @ExperimentalCoroutinesApi
    private val mTestCoroutineDispatcher = TestCoroutineDispatcher()


    @JvmField
    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val fortressDatabase : FortressDaoMock = FortressDaoMock()

    @ExperimentalCoroutinesApi
    @Before
    fun init() = runBlockingTest{
        val passwordEntity = PasswordEntity.getMock(id = 0)
        fortressDatabase.insert(passwordEntity)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `read Encrypted String from DB`() = mTestCoroutineDispatcher.runBlockingTest{
        assertNotNull(fortressDatabase.getEncryptedEntity(0))
        assertEquals(fortressDatabase.getEncryptedEntity(0), TestConstants.ENCRYPTED_STRING)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `delete PasswordEntity`() = runBlockingTest {
        assertNotNull(fortressDatabase.getEncryptedEntity(0))
//        fortressDatabase.insert(passwordEntity)
        fortressDatabase.delete(fortressDatabase.getPasswordDetails(0))
        assertNull(fortressDatabase.getEncryptedEntity(0))
    }


    @After
    fun tearDown(){
        fortressDatabase.clear()
    }


}