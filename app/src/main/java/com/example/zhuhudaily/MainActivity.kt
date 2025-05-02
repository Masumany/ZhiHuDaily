package com.example.zhuhudaily


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat.startActivity
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.example.zhuhudaily.ThemeManager.isDarkTheme
import com.example.zhuhudaily.ui.theme.BannerData
import com.example.zhuhudaily.ui.theme.GoneData
import com.example.zhuhudaily.ui.theme.ZhuHuDailyTheme
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    interface ApiService {
        @GET("api/zhihu")
        suspend fun getZhiHuNews(): BannerData
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

    interface ApiService2 {
        @GET("api/zhihu/get")
        suspend fun getZhiHuNews2(
            @Query("date") date: String,
        ): GoneData
    }

    object ApiClient2 {
        val apiService2: ApiService2 by lazy {
            val BASE_URL = "https://v3.alapi.cn/"
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
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofit.create(ApiService2::class.java)
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun BannerContent() {
        val scope = rememberCoroutineScope()
        var bannerData by remember { mutableStateOf<BannerData?>(null) }
        var isLoading by remember { mutableStateOf(true) }
        var error by remember { mutableStateOf<String?>(null) }

        LaunchedEffect(Unit) {
            isLoading = true
            scope.launch {
                try {
                    val response = ApiClient.apiService.getZhiHuNews()
                    bannerData = response
                    Log.d("MainActivity", "Request success: $response")
                } catch (e: Exception) {
                    error = e.message
                    Log.e("MainActivity", "Request error: ${e.message}", e)
                } finally {
                    isLoading = false
                }
            }
        }

        if (isLoading) {
            CircularProgressIndicator(
                color = Color(0xFF007AFF),
                modifier = Modifier
                    .size(48.dp)
            )
        } else if (error != null) {
            Text(text = error!!, color = Color.Red)
        } else if (bannerData != null) {
            val imageUrls = bannerData?.data?.topStories?.map { it.image }
            val bannerTitles = bannerData?.data?.topStories?.map { it.title }
            val bannerHint = bannerData?.data?.topStories?.map { it.hint }
            val pagerState = rememberPagerState(
                initialPage = 0,
                pageCount = {
                    imageUrls?.size ?: 0
                }
            )

            LaunchedEffect(Unit) {
                while (true) {
                    delay(2000)
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    if (pagerState.currentPage == imageUrls?.size?.minus(1) ?: 0) {
                        pagerState.animateScrollToPage(0)
                    }
                }
            }

            Box(
                modifier = Modifier
                    .width(360.dp)
                    .height(360.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .shadow(
                        elevation = 10.dp,
                        spotColor = Color.Black.copy(alpha = 0.3f),
                        ambientColor = Color.Black.copy(alpha = 0.2f)
                    )

            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomCenter)
                        .zIndex(1f)
                ) {
                    Text(
                        text = bannerTitles?.get(pagerState.currentPage) ?: "",
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    Text(
                        text = bannerHint?.get(pagerState.currentPage) ?: "",
                        style = TextStyle(fontSize = 14.sp),
                        color = Color(0xFFEDECEC),
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }
                val context = LocalContext.current
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth()
                        .clickable {
                            val position = pagerState.currentPage
                            val url = bannerData?.data?.topStories?.get(position)?.url ?: ""
                            val id = bannerData?.data?.topStories?.get(position)?.id?.toInt() ?: 0
                            val intent = Intent(context, ContentActivity::class.java)
                            intent.putExtra("url", url)
                            intent.putExtra("id", id)
                            context.startActivity(intent)
                        }
                ) { page ->
                    val imageUrl = imageUrls?.get(page)
                    if (imageUrl != null) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "轮播图",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                val indicatorOffset by animateOffsetAsState(
                    targetValue = Offset(
                        x = (pagerState.currentPage * 100).toFloat(),
                        y = 0F
                    ),
                    label = "indicatorOffset"
                )

                Box(
                    modifier = Modifier
                        .padding(bottom = 5.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    Box(
                        modifier = Modifier
                            .offset { IntOffset(x = indicatorOffset.x.toInt(), y = indicatorOffset.y.toInt()) }
                            .width(8.dp)
                            .height(8.dp)
                            .background(Color.White)
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZhuHuDailyTheme(darkTheme = ThemeManager.isDarkTheme) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Top(modifier = Modifier.padding(innerPadding))
                        BannerContent()
                        CombinedRVContent()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CombinedRVContent() {
    val scope = rememberCoroutineScope()
    var combinedData by remember { mutableStateOf<List<Any>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        isLoading = true
        scope.launch {
            try {
                val response1 = MainActivity.ApiClient.apiService.getZhiHuNews()
                val dates = getRecentDates(30)
                val responses = dates.map { date ->
                    scope.async {
                        MainActivity.ApiClient2.apiService2.getZhiHuNews2(date = date)
                    }
                }.awaitAll()
                combinedData = response1.data?.stories.orEmpty() + responses.flatMap { it.data?.stories.orEmpty() }
                Log.d("MainActivity", "Combined data: $combinedData")
            } catch (e: Exception) {
                error = e.message
                Log.e("MainActivity", "Request error: ${e.message}", e)
            } finally {
                isLoading = false
            }
        }
    }

    if (isLoading) {
        CircularProgressIndicator(
            color = Color(0xFF007AFF),
            modifier = Modifier
                .size(48.dp)
        )
    } else if (error != null) {
        Text(text = "Error: $error", color = Color.Red)
    } else if (combinedData != null) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            items(combinedData!!) { item ->
                when (item) {
                    is BannerData.Data.Story -> {
                        StoryCard(story = item)
                    }
                    is GoneData.Data.Story -> {
                        StoryCard(goneStory = item)
                    }
                }
            }
        }
    } else {
        Text(text = "No stories available.", modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center)
    }
}

@Composable
fun StoryCard(story: BannerData.Data.Story? = null, goneStory: GoneData.Data.Story? = null) {
    val title = story?.title ?: goneStory?.title ?: ""
    val hint = story?.hint ?: goneStory?.hint ?: ""
    val imageUrl = story?.images?.getOrNull(0) ?: goneStory?.images?.getOrNull(0)
    val url = story?.url ?: goneStory?.url

    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(
                elevation = 8.dp,
                spotColor = Color.Black.copy(alpha = 0.3f),
                ambientColor = Color.Black.copy(alpha = 0.2f)
            )
            .clickable {
                url?.let {
                    val id = story?.id?.toInt() ?: goneStory?.id?.toInt() ?: 0
                    val intent = Intent(context, ContentActivity::class.java)
                    intent.putExtra("url", it)
                    intent.putExtra("id", id)
                    context.startActivity(intent)
                }
            },
        colors = CardDefaults.cardColors(containerColor = if (ThemeManager.isDarkTheme) Color(0xFF1A1A1A) else Color.White),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                )
                Text(
                    text = hint,
                    style = TextStyle(fontSize = 14.sp, color = if (ThemeManager.isDarkTheme) Color.White else Color.Gray)
                )
            }
            imageUrl?.let { url ->
                AsyncImage(
                    model = url,
                    contentDescription = "图片",
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            }
        }
    }
}

private fun getRecentDates(days: Int): List<String> {
    val calendar = Calendar.getInstance()
    return (0 until days).map { daysAgo ->
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
        SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(calendar.time)
    }.reversed()
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("RememberReturnType")

@Composable
fun Top(modifier: Modifier) {
    val context = LocalContext.current
    var date by remember { mutableStateOf("") }
    var date2 by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        while (true) {
            val formatter = DateTimeFormatter.ofPattern("MMMM", Locale.CHINA)
            date = java.time.LocalDate.now().format(formatter)
            val formatter2 = SimpleDateFormat("d", Locale.getDefault())
            date2 = formatter2.format(Date())
            delay(1000)
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(horizontal = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 日期和分割线部分
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            val intent = Intent(context, CalendarActivity::class.java)
                            context.startActivity(intent)
                        },
                    text = date2,
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                    color = if (isDarkTheme) Color.White else Color(0xFF828282)
                )
                Text(
                    modifier = Modifier
                        .clickable {
                            val intent = Intent(context, CalendarActivity::class.java)
                            context.startActivity(intent)
                        },
                    text = date,
                    style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Thin),
                    color = if (isDarkTheme) Color.White else Color(0xFF828282)
                )
            }
                    Image(
                        painter = painterResource(id = R.drawable.line),
                        contentDescription = "分割线",
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.CenterVertically)
                    )
                     Text(
                             text = "晚上好!",
                         style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                      )
        }

        // 注册登录图标
        Image(
            painter = painterResource(id = R.drawable.ueser),
            contentDescription = "注册登录",
            modifier = Modifier
                .size(35.dp)
                .clickable {
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ZhuHuDailyTheme {
        Column {
            Top(Modifier.fillMaxWidth())
            CombinedRVContent()
        }
    }
}
