package com.ocwvar.mvvmplayground.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ocwvar.mvvmplayground.rules.TestCoroutineRule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
open class BaseUnitTest {

    // dispatcher for testing
    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    // testing scope
    private val testCoroutineScope = TestCoroutineScope(this.testCoroutineDispatcher)

    // rule for liveData testing
    @get:Rule
    val liveDataRule = InstantTaskExecutorRule()

    // rule for coroutine testing
    @get:Rule
    val testCoroutineRule = TestCoroutineRule(testCoroutineDispatcher)

    /**
     * @return CoroutineDispatcher a dispatcher for testing
     */
    fun getTestDispatcher(): CoroutineDispatcher = this.testCoroutineDispatcher

    /**
     * run coroutine in test scope
     *
     * @param function code needed run in testing coroutine
     */
    fun testingScope(function: suspend TestCoroutineScope.() -> Unit) {
        this.testCoroutineScope.runBlockingTest { function.invoke(this) }
    }

}