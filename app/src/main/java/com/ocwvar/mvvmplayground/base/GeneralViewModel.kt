package com.ocwvar.mvvmplayground.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GeneralViewModel: ViewModel() {
    val mLoadingStatusLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val mErrorStatusLiveData: MutableLiveData<Boolean> = MutableLiveData()
}