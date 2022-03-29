package com.ocwvar.mvvmplayground.base

interface BaseViewContract {

    /**
     * call when loading status has been changed
     *
     * @param isBeginLoading whether current is under loading
     */
    fun onLoadingStatusChanged(isBeginLoading: Boolean)

    /**
     * call when an error was occurred
     *
     * @param errorCode Error code
     */
    fun onError(errorCode: String)

}