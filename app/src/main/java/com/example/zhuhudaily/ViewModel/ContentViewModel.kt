package com.example.zhuhudaily.ViewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zhuhudaily.ApiClient3
import com.example.zhuhudaily.Data.CommentsData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContentViewModel : ViewModel() {
    var commentsData by mutableStateOf<CommentsData?>(null)
        private set
    var isLoading by mutableStateOf(true)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    fun fetchComments(articleId: String) {
       viewModelScope.launch(Dispatchers.IO) {//使用协程发起网络请求
            try {
                isLoading = true
                val response = ApiClient3.apiService3.getZhiHuComments(articleId)
                if (response.code == 200) {//状态码==200表示请求成功
                    commentsData = response
                    Log.d("CommentsData", "CommentsData: $response")
                } else {
                    error = "API response error: ${response.message}"
                    Log.e("Error", "API response error: ${response.message}")
                }
            } catch (e: Exception) {
                error = e.message ?: "An error occurred"
                Log.e("Error", "Error: $error", e)
            } finally {
                isLoading = false
            }
        }
    }
}
