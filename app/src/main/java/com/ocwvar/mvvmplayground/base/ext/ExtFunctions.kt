package com.ocwvar.mvvmplayground.base.ext

import android.view.View

/**
 * set OnClickListener with delay to prevent user double click globally
 *
 * @param block (view: View?) -> Unit OnClickEvent function
 * @receiver View
 */
fun View.setOnClickListenerWithDelay(block: (view: View?) -> Unit) {
    this.setOnClickListener(OnClickListenerWithDelay(block))
}