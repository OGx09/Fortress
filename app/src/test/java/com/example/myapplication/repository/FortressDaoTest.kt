package com.example.myapplication.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myapplication.FortressDaoMock
import com.example.myapplication.repository.database.PasswordEntity
import com.example.myapplication.utils.testhelper.TestConstants
import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import net.bytebuddy.matcher.ElementMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
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
    fun `test get password details`()= runBlockingTest {
        assertNotNull(fortressDatabase.getPasswordDetails(0))
        assertEquals(fortressDatabase.getPasswordDetails(0)?.website, TestConstants.WEBSITE)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `delete PasswordEntity`() = runBlockingTest {
        val passwordDetails = fortressDatabase.getPasswordDetails(0)
        assertNotNull(passwordDetails)
        fortressDatabase.delete(passwordDetails!!)
        assertNull(fortressDatabase.getEncryptedEntity(0))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `test insert()`() = runBlockingTest {
        fortressDatabase.clear()
        assertNull(fortressDatabase.getPasswordDetails(0))
        fortressDatabase.insert(PasswordEntity.getMock(id = 1))
        assertNotNull(fortressDatabase.getPasswordDetails(1))
    }


    @ExperimentalCoroutinesApi
    @Test
    fun `test get all saved password entities`() = runBlockingTest {
        for (i in 0..7){
            fortressDatabase.insert(PasswordEntity.getMock(id = i))
        }
        val passwords = fortressDatabase.getAllEncryptedPassword().value
        assertNotNull(passwords)
        assertEquals(fortressDatabase.count(), 8)
    }

    @After
    fun tearDown(){
        fortressDatabase.clear()
    }


}