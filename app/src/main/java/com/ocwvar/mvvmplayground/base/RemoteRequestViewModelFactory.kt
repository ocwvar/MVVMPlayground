package com.ocwvar.mvvmplayground.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ocwvar.mvvmplayground.network.NetworkRequest
import com.ocwvar.mvvmplayground.network.Remote
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

open class RemoteRequestViewModelFactory: ViewModelProvider.Factory {

    /**
     * Creates a new instance of the given `Class`.
     *
     * @param modelClass a `Class` whose instance is requested
     * @return a newly created ViewModel
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            Remote.Models::class.java,
            CoroutineDispatcher::class.java
        ).newInstance(
            NetworkRequest.remoteModels,
            Dispatchers.IO
        )
    }

}