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
    suspend fun getZhiHuNews(): BannerData//挂起函数。可在协程中调用
}

interface ApiService2 {
    @GET("api/zhihu/get")
    suspend fun getZhiHuNews2(
        @Query("date") date: String//查询字符
    ): GoneData
}

interface ApiService3 {
    @GET("api/zhihu/short_comments")
    suspend fun getZhiHuComments(
        @Query("id") id: String
    ): CommentsData
}

private fun <T> createRetrofitService(baseUrl: String, apiInterface: Class<T>): T {//创建 Retrofit 服务实例
    val token = "hnq0tkp4bowkjcqtbn5xxd4qy1kjoj"
    val tokenInterceptor = Interceptor { chain ->//拦截所有的网络请求，并在请求头中添加认证信息
        val originalRequest = chain.request()//获取原始的请求对象
        val newRequest = originalRequest.newBuilder()//创建一个原始请求的构建器
            .addHeader("Authorization", "Bearer $token")
            .build()
        chain.proceed(newRequest)//继续处理新的请求
    }

    val okHttpClient = OkHttpClient.Builder()//创建 OkHttpClient 实例
        .addInterceptor(tokenInterceptor)//将之前创建的拦截器添加到 OkHttpClient 中
        .build()

    val retrofit = Retrofit.Builder()//创建 Retrofit 实例
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())//服务器返回的 JSON 数据转换为 Kotlin 对象
        .build()

    return retrofit.create(apiInterface)//根据传入的 apiInterface 创建对应的 Retrofit 服务实例，并将其返回
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