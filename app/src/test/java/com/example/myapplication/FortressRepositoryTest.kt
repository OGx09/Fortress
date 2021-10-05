package com.example.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myapplication.utils.EncryptionUtils
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock

@RunWith(JUnit4::class)
class FortressRepositoryTest {

    @JvmField
    @Rule
    val instanceExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val mTestCoroutineDispatcher = TestCoroutineDispatcher()

    @ExperimentalCoroutinesApi
    val fortressRepository = RepositoryMock(mock(EncryptionUtils::class.java), mTestCoroutineDispatcher)

    @ExperimentalCoroutinesApi
    @Test
    fun `test fetchUsername()`() = runBlockingTest {
        val username = fortressRepository.fetchUsername().first()
        assertNotNull(username)
        assertEquals(username, "Gbenga")
    }
}