package com.example.myapplication

import com.example.myapplication.repository.models.LoadingState
import junit.framework.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LoadingStateTest {

    @Test
    fun `test loading states success`(){
        val loading = LoadingState(data = null)
        assertTrue(loading.isLoading)
        val successResult = LoadingState(data = "HelloWord")
        assertFalse(successResult.isLoading)
        assertEquals(successResult.data, "HelloWord")
    }

    @Test
    fun `test loading state failed`(){
        val loading = LoadingState(data = null)
        assertTrue(loading.isLoading)
        val failedResult = LoadingState<Any>(error = "Failed response")
        assertFalse(failedResult.isLoading)
        assertEquals(failedResult.error, "Failed response")
    }

}