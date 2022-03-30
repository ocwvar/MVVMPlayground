package com.ocwvar.mvvmplayground.base.ext

import android.view.View

class OnClickListenerWithDelay(private val block: (view: View?) -> Unit) : View.OnClickListener {

    companion object {
        private const val CLICK_INTERVAL: Long = 800L
        private var LAST_CLICK_TIME: Long = 0L
    }

    private val isQuickClick: Boolean
        get() = System.currentTimeMillis() - LAST_CLICK_TIME <= CLICK_INTERVAL

    override fun onClick(v: View?) {
        if (this.isQuickClick) {
            return
        }

        LAST_CLICK_TIME = System.currentTimeMillis()
        this.block.invoke(v)
    }

}