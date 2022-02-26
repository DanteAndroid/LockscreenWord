package com.danteandroi.lockscreenword.net

import com.danteandroi.lockscreenword.data.AppInfo
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface AppApi {

    @GET("app.json")
    fun getAppInfo(): Flow<AppInfo>

    @GET
    @Streaming
    fun download(@Url apkUrl: String): Flow<ResponseBody>

}