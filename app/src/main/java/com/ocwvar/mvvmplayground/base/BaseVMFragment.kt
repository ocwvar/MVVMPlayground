package com.ocwvar.mvvmplayground.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.ocwvar.mvvmplayground.R

abstract class BaseVMFragment<T : BaseViewModel> : BaseFragment(), BaseViewContract {

    private lateinit var viewModel: T

    /**
     * @return current view model of this page
     */
    abstract fun onCreateViewModel(): T

    /**
     * @return view model of this page
     */
    fun getViewModel(): T {
        return this.viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.viewModel = onCreateViewModel()

        //observer error status and will notice page when error code has been given
        this.viewModel.errorStatusLiveData.observe(this.viewLifecycleOwner) { errorCode: String ->
            this.onError(errorCode)
        }

        //observer loading status and will notice page when status changed
        this.viewModel.loadingStatusLiveData.observe(this.viewLifecycleOwner) { isLoading: Boolean ->
            this.onLoadingStatusChanged(isLoading)
        }
    }
}