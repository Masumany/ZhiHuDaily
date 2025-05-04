package com.example.zhuhudaily

import com.example.zhuhudaily.Data.BannerData
import com.example.zhuhudaily.Data.GoneData
import com.example.zhuhudaily.Data.CommentsData
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/zhihu")
    suspend fun getZhiHuNews(): BannerData
}

interface ApiService2 {
    @GET("api/zhihu/get")
    suspend fun getZhiHuNews2(
        @Query("date") date: String
    ): GoneData
}

interface ApiService3 {
    @GET("api/zhihu/short_comments")
    suspend fun getZhiHuComments(
        @Query("id") id: String
    ): CommentsData
}

private fun <T> createRetrofitService(baseUrl: String, apiInterface: Class<T>): T {
    val token = "hnq0tkp4bowkjcqtbn5xxd4qy1kjoj"
    val tokenInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        chain.proceed(newRequest)
    }

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(tokenInterceptor)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(apiInterface)
}

// ApiClient 实例
object ApiClient {
    private const val BASE_URL = "https://v3.alapi.cn/"
    val apiService: ApiService by lazy {
        createRetrofitService(BASE_URL, ApiService::class.java)
    }
}

object ApiClient2 {
    private const val BASE_URL = "https://v3.alapi.cn/"
    val apiService2: ApiService2 by lazy {
        createRetrofitService(BASE_URL, ApiService2::class.java)
    }
}

object ApiClient3 {
    private const val BASE_URL = "https://v3.alapi.cn/"
    val apiService3: ApiService3 by lazy {
        createRetrofitService(BASE_URL, ApiService3::class.java)
    }
}