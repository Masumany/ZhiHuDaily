package com.example.zhuhudaily.ViewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zhuhudaily.ApiClient
import com.example.zhuhudaily.ApiClient2
import com.example.zhuhudaily.View.MainActivity
import com.example.zhuhudaily.Data.BannerData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    var bannerData by mutableStateOf<BannerData?>(null)//可变的对象
        private set//只能在 MainViewModel 类内部被修改
    var combinedData by mutableStateOf<List<Any>?>(null)
        private set
    var isLoadingBanner by mutableStateOf(true)
        private set
    var isLoadingCombined by mutableStateOf(true)
        private set
    var bannerError by mutableStateOf<String?>(null)
        private set
    var combinedError by mutableStateOf<String?>(null)
        private set

    fun fetchBannerData() {
        viewModelScope.launch(Dispatchers.IO) {//启动协程
            try {
                isLoadingBanner = true
                val response = ApiClient.apiService.getZhiHuNews()
                bannerData = response
                Log.d("MainActivity", "Request success: $response")
            } catch (e: Exception) {
                bannerError = e.message
                Log.e("MainActivity", "Request error: ${e.message}", e)
            } finally {
                isLoadingBanner = false
            }
        }
    }

    fun fetchCombinedData() {
       viewModelScope.launch(Dispatchers.IO) {
            try {
                isLoadingCombined = true
                val response1 = ApiClient.apiService.getZhiHuNews()
                val dates = MainActivity.getRecentDates(7)
                val responses = dates.map { date:String ->
                    viewModelScope.async {//异步发起网络请求
                        ApiClient2.apiService2.getZhiHuNews2(date = date)
                    }
                }.awaitAll()
                combinedData = response1.data?.stories.orEmpty() + responses.flatMap { it.data?.stories.orEmpty() }//让多个日期的集合合并为一个
                Log.d("MainActivity", "Combined data: $combinedData")
            } catch (e: Exception) {
                combinedError = e.message
                Log.e("MainActivity", "Request error: ${e.message}", e)
            } finally {
                isLoadingCombined = false
            }
        }
    }
}