package com.example.zhuhudaily.View

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
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.zhuhudaily.ThemeManager.isDarkTheme
import com.example.zhuhudaily.Data.BannerData
import com.example.zhuhudaily.Data.GoneData
import com.example.zhuhudaily.R
import com.example.zhuhudaily.ThemeManager
import com.example.zhuhudaily.ViewModel.MainViewModel
import com.example.zhuhudaily.ui.theme.ZhuHuDailyTheme
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZhuHuDailyTheme(darkTheme = ThemeManager.isDarkTheme) {
                val viewModel: MainViewModel = viewModel()
                val scope = rememberCoroutineScope()//创建一个协程作用域

                LaunchedEffect(Unit) {//启动协程获取banner和combined数据
                    viewModel.fetchBannerData()
                    viewModel.fetchCombinedData()
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Top(modifier = Modifier.padding(innerPadding))
                        BannerContent(viewModel = viewModel)
                        CombinedRVContent(viewModel = viewModel)
                    }
                }
            }
        }
    }


companion object {//创建一个伴生对象
    fun getRecentDates(days: Int): List<String> {
        val calendar = Calendar.getInstance()//创建Calendar类的实例
        return (0 until days).map { daysAgo ->
            calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)//得到过去的日期
            SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(calendar.time)//格式化日期
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun BannerContent(viewModel: MainViewModel) {
        val scope = rememberCoroutineScope()//创建一个协程域
        val pagerState = rememberPagerState(//记住页码状态
            initialPage = 0,
            pageCount = {
                viewModel.bannerData?.data?.topStories?.size ?: 0//若有即为此页，若无则为0
            }
        )

        LaunchedEffect(Unit) {//组件首次组合时启动一个新协程
            while (true) {
                delay(2000)//挂起函数，停2s
                pagerState.animateScrollToPage(pagerState.currentPage + 1)//滑动到指定页面
                if (pagerState.currentPage == viewModel.bannerData?.data?.topStories?.size?.minus(1) ?: 0) {
                    delay(2000)//停2s
                    pagerState.animateScrollToPage(0)//如果到了最后一页，则滑动到第一页
                }
            }
        }

        if (viewModel.isLoadingBanner) {
            CircularProgressIndicator(
                color = Color(0xFF007AFF),
                modifier = Modifier
                    .size(48.dp)
            )//加载的小圈圈
        } else if (viewModel.bannerError != null) {
            Text(text = viewModel.bannerError!!, color = Color.Red)
        } else if (viewModel.bannerData != null) {
            val imageUrls = viewModel.bannerData?.data?.topStories?.map { it.image }
            val bannerTitles = viewModel.bannerData?.data?.topStories?.map { it.title }
            val bannerHint = viewModel.bannerData?.data?.topStories?.map { it.hint }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .clip(RoundedCornerShape(10.dp))
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
                        color = if (isDarkTheme) Color.White else Color.White,
                        modifier = Modifier.padding(bottom = 10.dp)//banner的标题
                    )
                    Text(
                        text = bannerHint?.get(pagerState.currentPage) ?: "",
                        style = TextStyle(fontSize = 14.sp),
                        color = Color(0xFF868686),
                        modifier = Modifier.padding(bottom = 10.dp)//banner的作者那一行
                    )
                }
                val context = LocalContext.current
                HorizontalPager(//水平的viewpage
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth()
                        .clickable {
                            val position = pagerState.currentPage
                            val url =
                                viewModel.bannerData?.data?.topStories?.get(position)?.url ?: ""
                            val id =
                                viewModel.bannerData?.data?.topStories?.get(position)?.id?.toInt()
                                    ?: 0
                            val intent = Intent(context, ContentActivity::class.java)
                            intent.putExtra("url", url)
                            intent.putExtra("id", id)
                            context.startActivity(intent)//给ContentActivity传入url和id
                        }
                ) { page ->//显示图片
                    val imageUrl = imageUrls?.get(page)
                    if (imageUrl != null) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "轮播图",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(320.dp),
                            contentScale = androidx.compose.ui.layout.ContentScale.FillWidth//设置图片的缩放模式：宽填满父容器
                        )//加载图片
                    }
                }

                val indicatorOffset by animateOffsetAsState(
                    targetValue = Offset(//动画的目标：获取当前页的偏移量x：间距100
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
                            .offset {
                                IntOffset(
                                    x = indicatorOffset.x.toInt(),//因为offset接受float型，所以需要转换为int型
                                    y = indicatorOffset.y.toInt()
                                )
                            }
                            .width(8.dp)
                            .height(8.dp)
                            .background(Color.White, shape = CircleShape)//圆形，方形有点丑
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun CombinedRVContent(viewModel: MainViewModel) {
        if (viewModel.isLoadingCombined) {
            CircularProgressIndicator(
                color = Color(0xFF007AFF),
                modifier = Modifier
                    .size(48.dp)
            )//加载的小圈圈
        } else if (viewModel.combinedError != null) {
            Text(text = "Error: ${viewModel.combinedError}", color = Color.Red)
        } else if (viewModel.combinedData != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                items(viewModel.combinedData!!) { item ->//遍历列表数据
                    when (item) {//根据item的类型来显示
                        is BannerData.Data.Story -> {
                            StoryCard(story = item, combinedData = viewModel.combinedData)
                        }

                        is GoneData.Data.Story -> {
                            StoryCard(goneStory = item, combinedData = viewModel.combinedData)
                        }

                    }
                }
            }
        } else {
            Text(
                text = "No stories available.",
                modifier = Modifier.fillMaxSize(),
                textAlign = TextAlign.Center
            )
        }
    }

    @Composable
    fun StoryCard(
        story: BannerData.Data.Story? = null,
        goneStory: GoneData.Data.Story? = null,
        combinedData: List<Any>?
    ) {
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
                    spotColor = Color.Blue.copy(alpha = 0.3f),//阴影中较亮的颜色
                    ambientColor = Color.Black.copy(alpha = 0.2f)//阴影中不较亮的颜色
                )
                .clickable {
                    url?.let {//url不为null时执行
                        val id = story?.id?.toInt() ?: goneStory?.id?.toInt() ?: 0
                        Log.d("StoryCard", "Going to launch ContentActivity with url: $it, id: $id")
                        val intent = Intent(context, ContentActivity::class.java)
                        intent.putExtra("url", it)
                        intent.putExtra("id", id)//向ContentActivity传入url和id
                        val articleList = combinedData?.map { item ->
                            when (item) {
                                is BannerData.Data.Story -> Article(item.id.toInt(), item.url)
                                is GoneData.Data.Story -> Article(item.id.toInt(), item.url)
                                else -> null
                            }//将列表中的元素转换为Article对象，并过滤掉null元素
                        }?.filterNotNull()//去除空元素
                        intent.putParcelableArrayListExtra("articleList", ArrayList(articleList))//列表数据放在Intent中
                        context.startActivity(intent)
                    }
                },
            colors = CardDefaults.cardColors(
                containerColor = if (ThemeManager.isDarkTheme) Color(
                    0xFF1A1A1A
                ) else Color.White
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = title,
                        maxLines = 2,
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)//标题
                    )
                    Text(
                        text = hint,
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = if (ThemeManager.isDarkTheme) Color.White else Color.Gray//提示
                        )
                    )
                }
                imageUrl?.let { url ->
                    AsyncImage(
                        model = url,
                        contentDescription = "图片",
                        modifier = Modifier
                            .width(80.dp)
                            .height(80.dp)
                            .clip(RoundedCornerShape(10.dp))//加载图片
                    )
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("RememberReturnType")
    @Composable
    fun Top(modifier: Modifier) {
        val context = LocalContext.current
        var date by remember { mutableStateOf("") }
        var date2 by remember { mutableStateOf("") }
        val scope = rememberCoroutineScope()//定义一个协程

        LaunchedEffect(Unit) {//组件首次组合时启动一个新协程
            while (true) {
                val formatter = DateTimeFormatter.ofPattern("MMMM", Locale.CHINA)//格式
                date = java.time.LocalDate.now().format(formatter)
                val formatter2 = SimpleDateFormat("d", Locale.getDefault())
                date2 = formatter2.format(Date())
                delay(1000)//挂起函数，每一1000s刷新一次
            }
        }

        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp)
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
                    text = "知乎日报",
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
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
                CombinedRVContent(viewModel = viewModel())
            }
        }
    }
}}

