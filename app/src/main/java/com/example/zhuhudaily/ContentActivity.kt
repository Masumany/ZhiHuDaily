package com.example.zhuhudaily

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.example.zhuhudaily.ui.theme.CommentsData
import com.example.zhuhudaily.ui.theme.ZhuHuDailyTheme
import kotlinx.coroutines.async
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class ContentActivity : ComponentActivity() {
    interface ApiService {
        @GET("api/zhihu/short_comments")
        suspend fun getZhiHuComments(
            @Query("id") id: String
        ): CommentsData
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

    @Composable
    fun Comments(modifier: Modifier, articleId: String) {
        val scope = rememberCoroutineScope()
        var commentsData by remember { mutableStateOf<CommentsData?>(null) }
        var isLoading by remember { mutableStateOf(true) }
        var error by remember { mutableStateOf<String?>(null) }

        LaunchedEffect(Unit) {
            isLoading = true
            try {
                val response = scope.async { ApiClient.apiService.getZhiHuComments(articleId) }.await()
                val rawResponse = ApiClient.apiService.getZhiHuComments(articleId).toString()
                Log.d("RawResponse", "RawResponse: $rawResponse")
                commentsData = response
                Log.d("CommentsData", "CommentsData: $response")
            } catch (e: Exception) {
                error = e.message ?: "An error occurred"
                Log.e("Error", "Error: $error", e)
            } finally {
                isLoading = false
            }
        }

        if (isLoading) {
            CircularProgressIndicator(
                color = Color(0xFF007AFF),
                modifier = Modifier
                    .size(48.dp)

            )
        } else if (error != null) {
            Text(
                text = "Error: $error",
                modifier = Modifier
                    .padding(16.dp),
                style = TextStyle(fontSize = 16.sp)
            )
        } else {
            Text(
                text = "评论",
                color = if (ThemeManager.isDarkTheme) Color.White else Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (ThemeManager.isDarkTheme) Color.Black else Color.White),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(if (ThemeManager.isDarkTheme) Color.Black else Color.White)
            ) {
                commentsData?.data?.comments?.let { comments ->
                    items(comments) { comment ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            AsyncImage(
                                model = comment.avatar,
                                contentDescription = "Avatar",
                                modifier = Modifier
                                    .size(40.dp)
                                    .align(Alignment.CenterVertically)
                            )
                            Column(
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .weight(1f)
                                    .align(Alignment.CenterVertically)
                            ) {
                                Text(
                                    color = if (ThemeManager.isDarkTheme) Color.White else Color.Black,
                                    text = comment.author,
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    color = if (ThemeManager.isDarkTheme) Color.White else Color.Black,
                                    text = comment.content,
                                    style = TextStyle(fontSize = 14.sp),
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    }
                }?: run {
                    item {
                        Text(
                            text = "No comments available.",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
,                            style = TextStyle(fontSize = 16.sp)
                        )
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZhuHuDailyTheme(darkTheme = ThemeManager.isDarkTheme) {
                val url = intent.getStringExtra("url") ?: ""
                val articleId = intent.getIntExtra("id", 0)
                Log.d("ArticleId", "ArticleId: $articleId")
                Column {
                    Content(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(570.dp)
                            .background(if (ThemeManager.isDarkTheme) Color.Black else Color.White),
                        url = url
                    )
                    Comments(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .background(if (ThemeManager.isDarkTheme) Color.Black else Color.White),
                        articleId = articleId.toString()
                    )
                }
            }
        }
    }
}

@Composable
fun Content(
    modifier: Modifier,
    url: String
) {
    Column(modifier = modifier) {
        // 返回按钮
        val context = LocalContext.current
        Image(
            painter = painterResource(if (ThemeManager.isDarkTheme) R.drawable.lightback else R.drawable.back),
            contentDescription = "返回",
            modifier = Modifier
                .padding(16.dp)
                .size(30.dp)
                .clickable {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }
        )

        // WebView 组件用于加载URL内容
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    layoutParams = android.view.ViewGroup.LayoutParams(
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = true
                    loadUrl(url)
                }
            },
            modifier = Modifier
                .height(200.dp)
                .weight(1f)
        )
    }
}
