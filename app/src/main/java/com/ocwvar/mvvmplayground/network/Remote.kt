package com.ocwvar.mvvmplayground.network

import com.ocwvar.mvvmplayground.base.GeneralModel
import com.ocwvar.mvvmplayground.model.AccountModel
import com.ocwvar.mvvmplayground.model.HomePageModel
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

class Remote(retrofit: Retrofit) {

    val get: Models = retrofit.create(Models::class.java)

    interface Models {
        @GET("/main/user/{proxy}")
        suspend fun getUserInfoModel(@Path(value = "proxy") path: String): GeneralModel<AccountModel.UserInfoModel>

        @GET("/main/user/{userName}/{proxy}")
        suspend fun getStatusModel(
            @Path(value = "userName") userName: String,
            @Path(value = "proxy") proxy: String
        ): GeneralModel<AccountModel.StatusModel>

        @GET("/main/home/{proxy}")
        suspend fun getHomeModel(@Path(value = "proxy") proxy: String): GeneralModel<HomePageModel.Model>

        @GET
        suspend fun getImageBytes(@Url url: String): ResponseBody
    }

}