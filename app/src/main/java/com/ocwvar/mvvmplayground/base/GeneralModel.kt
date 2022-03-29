package com.ocwvar.mvvmplayground.base

data class GeneralModel<T>(
    val code: String,
    private val data: T?
) {

    fun hasData(): Boolean = data != null

    /**
     * get result data
     *
     * @return object of [T]
     * @throws IllegalAccessException When data is NULL
     */
    fun getData(): T {
        if (this.data == null) {
            throw IllegalAccessException("No data !! Code is $code")
        }

        return data
    }

}
