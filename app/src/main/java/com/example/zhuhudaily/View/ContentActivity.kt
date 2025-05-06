package com.example.zhuhudaily.View

import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.zhuhudaily.R
import com.example.zhuhudaily.ThemeManager
import com.example.zhuhudaily.ViewModel.ContentViewModel
import com.example.zhuhudaily.ui.theme.ZhuHuDailyTheme

class ContentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZhuHuDailyTheme {
                val viewModel: ContentViewModel = viewModel()
                val url = intent.getStringExtra("url") ?: ""
                val id = intent.getIntExtra("id", 0)
                val articleList = intent.getParcelableArrayListExtra<Article>("articleList") ?: emptyList()//接受从MainActivity中获取的articleList
                var currentIndex by remember { mutableIntStateOf(articleList.indexOfFirst { it.id == id }) }//遍历列表返回弟弟一个元素的索引，没有找到符合条件的返回-1
                if (currentIndex == -1) {
                    currentIndex = 0//没有找到就为0
                }
                var isExpanded by remember { mutableStateOf(false) }//关于评论折叠展开

                LaunchedEffect(articleList.getOrNull(currentIndex)?.id?.toString() ?: id.toString()) {
                    viewModel.fetchComments(articleList.getOrNull(currentIndex)?.id?.toString() ?: id.toString())
                }//协程体

                Column {
                    Content(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(if (isExpanded) 400.dp else 680.dp)
                            .background(if (ThemeManager.isDarkTheme) Color.Black else Color.White),
                        url = articleList.getOrNull(currentIndex)?.url ?: url,
                        onPreviousClick = {
                            if (currentIndex > 0) {
                                currentIndex--
                                if(currentIndex == 0){
                                    Toast.makeText(this@ContentActivity, "已到文章列表的起始", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        onNextClick = {
                            if (currentIndex < articleList.size - 1) {
                                currentIndex++
                                if (currentIndex == articleList.size - 1){
                                    Toast.makeText(this@ContentActivity, "已到文章列表的末尾", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    )//切换上下篇文章的方法
                    Comments(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (ThemeManager.isDarkTheme) Color.Black else Color.White),
                        articleId = articleList.getOrNull(currentIndex)?.id?.toString() ?: id.toString(),
                        isExpanded = isExpanded,
                        onExpandChange = { isExpanded = it },
                        viewModel = viewModel
                    )//评论展开折叠的方法
                }
            }
        }
    }
}

data class Article(val id: Int, val url: String) : Parcelable {//不同组件中的传递
constructor(parcel: Parcel) : this(//从 Parcel 对象中读取数据并创建 Article 对象
    parcel.readInt(),//id
    parcel.readString()!!//url
)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(url)
    }//Article 对象的属性写入 Parcel 对象

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Article> {//伴生对象，实现 Parcelable.Creator 接口来创建 Article 对象的实例
    override fun createFromParcel(parcel: Parcel): Article {
        return Article(parcel)
    }

        override fun newArray(size: Int): Array<Article?> {
            return arrayOfNulls(size)//创建一个指定大小的 Article 对象数组，初始值都为 null
        }
    }
}

@Composable
fun Content(
    modifier: Modifier = Modifier,
    url: String,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit
) {//文章内容
    var currentUrl by remember { mutableStateOf(url) }
    Column(modifier = modifier) {
        val context = LocalContext.current
        Image(
            painter = painterResource(id = android.R.drawable.ic_menu_close_clear_cancel),
            contentDescription = "返回",
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }
        )//返回
        Box(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .zIndex(1f)
                    .align(Alignment.Center),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(painter = painterResource(id = R.drawable.before), contentDescription = "上一篇",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .clickable {
                            onPreviousClick()
                        })

                Image(painter = painterResource(id = R.drawable.after), contentDescription = "下一篇",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .clickable {
                            onNextClick()
                        })

            }//切换上下篇按钮

            AndroidView(
                factory = { context ->//webView实例
                    WebView(context).apply {
                        layoutParams = android.view.ViewGroup.LayoutParams(
                            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                            android.view.ViewGroup.LayoutParams.MATCH_PARENT
                        )//设置webView的参数
                        webViewClient = object : WebViewClient() {
                            override fun onReceivedError(
                                view: WebView?,
                                request: WebResourceRequest?,
                                error: WebResourceError?
                            ) {
                                super.onReceivedError(view, request, error)
                                Log.e("ContentActivity", "WebView error: ${error?.description}")
                            }
                        }
                        settings.javaScriptEnabled = true//启用js的支持
                        loadUrl(currentUrl)//加载指定的url
                    }
                },
                update = { webView ->//更新webView
                    if (webView.url != currentUrl) {
                        webView.loadUrl(currentUrl)
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
            )

        }//在app内打开网页

        LaunchedEffect(url) {//启动协程，保证url与文章的对应
            currentUrl = url
        }
    }
}

@Composable
fun Comments(
    modifier: Modifier = Modifier,
    articleId: String,
    isExpanded: Boolean,
    onExpandChange: (Boolean) -> Unit,
    viewModel: ContentViewModel
) {
    val scope = rememberCoroutineScope()
    val screenHeight = LocalContext.current.resources.displayMetrics.heightPixels.dp
    val expandedHeight = screenHeight / 2
    val collapsedHeight = 100.dp

    if (viewModel.isLoading) {
        CircularProgressIndicator(
            color = Color(0xFF007AFF),
            modifier = Modifier
                .size(48.dp)
        )//加载的小圈圈
    } else if (viewModel.error != null) {
        Text(
            text = "Error: ${viewModel.error}",
            modifier = Modifier
                .padding(16.dp),
            style = TextStyle(fontSize = 16.sp)
        )
    } else {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(if (isExpanded) expandedHeight else collapsedHeight)
                .background(if(ThemeManager.isDarkTheme) Color.Black else Color.White),
            contentAlignment = Alignment.BottomCenter
        ) {
            if (isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .background(if(ThemeManager.isDarkTheme) Color.Black else Color.White)
                ) {
                    Text(
                        text = "评论",
                        color = if (ThemeManager.isDarkTheme) Color.White else Color.Black,
                        modifier = Modifier.padding(top = 16.dp),
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )//评论标题
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        viewModel.commentsData?.data?.comments?.let { comments ->
                            items(comments) { comment ->//遍历每一个可组合项
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
                                            .clip(CircleShape)
                                    )//用户头像的加载
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
                                        )//作者
                                        Text(
                                            color = if (ThemeManager.isDarkTheme) Color.White else Color.Black,
                                            text = comment.content,
                                            style = TextStyle(fontSize = 14.sp),
                                            modifier = Modifier.padding(top = 8.dp)
                                        )//内容
                                    }
                                }
                            }
                        } ?: run {//评论为空时
                            item {
                                Text(
                                    text = "No comments available.",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    style = TextStyle(fontSize = 16.sp)
                                )
                            }
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = if (isExpanded) R.drawable.comment else R.drawable.comment_expanded),
                    contentDescription = if (isExpanded) "收起评论" else "展开评论",
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(40.dp)
                        .clickable {
                            onExpandChange(!isExpanded)
                        }
                )
            }
        }
    }
}
