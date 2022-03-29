package com.ocwvar.mvvmplayground.rules

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class TestCoroutineRule(private val testingDispatcher: TestCoroutineDispatcher) : TestWatcher() {

    override fun starting(description: Description?) {
        super.starting(description)
        // set main dispatcher to our TestCoroutineDispatcher
        Dispatchers.setMain(this.testingDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}