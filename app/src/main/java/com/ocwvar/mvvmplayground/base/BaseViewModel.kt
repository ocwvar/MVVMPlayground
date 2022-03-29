package com.ocwvar.mvvmplayground.base

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ocwvar.mvvmplayground.general.Constant
import kotlinx.coroutines.*

abstract class BaseViewModel(
    private val workerDispatcher: CoroutineDispatcher
) : ViewModel(), BaseViewModelContact {

    //loading status notice live data
    private val mLoadingStatusLiveData: MutableLiveData<Boolean> = MutableLiveData()

    //error status notice live data
    private val mErrorStatusLiveData: MutableLiveData<String> = MutableLiveData()

    //unhandled exception handler for viewModelScope
    private val unhandledExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            //any unhandled exception that occurred during [fetchJob] will end up here
            println("WE HAVE AN UNHANDLED EXCEPTION: $throwable")
            this.mLoadingStatusLiveData.postValue(false)
            this.mErrorStatusLiveData.postValue(Constant.ErrorCode.UNKNOWN + "  " + throwable)
        }

    //current remote module fetch job
    private var fetchJob: Job? = null

    val loadingStatusLiveData: LiveData<Boolean>
        get() = this.mLoadingStatusLiveData

    val errorStatusLiveData: LiveData<String>
        get() = this.mErrorStatusLiveData

    /**
     * begin to fetch model from remote
     *
     * @param block request function block
     */
    protected fun beginFetch(@WorkerThread block: suspend () -> Unit) {
        val canStartNewJob: Boolean = fetchJob?.let {
            it.isCancelled || it.isCompleted
        } ?: true

        // if last job is still running, we skip this request
        if (!canStartNewJob) return

        // run me !!!
        this.fetchJob = viewModelScope.launch(this.unhandledExceptionHandler) {
            mLoadingStatusLiveData.postValue(true)

            /*
                The workerDispatcher default is [Dispatchers.IO]
                come from [RemoteRequestViewModelFactory]

                it will become [TestCoroutineDispatcher] when run in unit test
             */
            withContext(workerDispatcher) { block.invoke() }

            mLoadingStatusLiveData.postValue(false)
        }
    }

    /**
     * check if this response was error occurred.
     * will notice Ui with error code if any error occurred
     *
     * @return has error
     */
    internal fun <T> hasError(responseModel: GeneralModel<T>): Boolean {
        return if (responseModel.code != Constant.ErrorCode.NO_ERROR) {
            postError(responseModel.code)
            true
        } else {
            false
        }
    }

    internal fun postError(errorString: String) {
        this.mErrorStatusLiveData.postValue(errorString)
    }

}