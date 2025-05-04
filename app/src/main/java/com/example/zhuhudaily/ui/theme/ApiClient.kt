package com.example.zhuhudaily

import com.example.zhuhudaily.ui.theme.BannerData
import com.example.zhuhudaily.ui.theme.GoneData
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
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

object ApiClient {
    private const val BASE_URL = "https://v3.alapi.cn/"
    private val token = "hnq0tkp4bowkjcqtbn5xxd4qy1kjoj"

    val apiService: ApiService by lazy {
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
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

object ApiClient2 {
    private const val BASE_URL = "https://v3.alapi.cn/"
    private val token = "hnq0tkp4bowkjcqtbn5xxd4qy1kjoj"

    val apiService2: ApiService2 by lazy {
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
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService2::class.java)
    }
}