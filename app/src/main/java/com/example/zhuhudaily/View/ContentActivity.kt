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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
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
                val articleList = intent.getParcelableArrayListExtra<Article>("articleList") ?: emptyList()
                var currentIndex by remember { mutableIntStateOf(articleList.indexOfFirst { it.id == id }) }
                if (currentIndex == -1) {
                    currentIndex = 0
                }
                var isExpanded by remember { mutableStateOf(false) }

                LaunchedEffect(articleList.getOrNull(currentIndex)?.id?.toString() ?: id.toString()) {
                    viewModel.fetchComments(articleList.getOrNull(currentIndex)?.id?.toString() ?: id.toString(), this)
                }

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
                            }
                        },
                        onNextClick = {
                            if (currentIndex < articleList.size - 1) {
                                currentIndex++
                            }
                        }
                    )
                    Comments(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (ThemeManager.isDarkTheme) Color.Black else Color.White),
                        articleId = articleList.getOrNull(currentIndex)?.id?.toString() ?: id.toString(),
                        isExpanded = isExpanded,
                        onExpandChange = { isExpanded = it },
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

data class Article(val id: Int, val url: String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Article> {
        override fun createFromParcel(parcel: Parcel): Article {
            return Article(parcel)
        }

        override fun newArray(size: Int): Array<Article?> {
            return arrayOfNulls(size)
        }
    }
}

@Composable
fun Content(
    modifier: Modifier = Modifier,
    url: String,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit
) {
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
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onPreviousClick,
                colors = ButtonDefaults.buttonColors(if (ThemeManager.isDarkTheme) Color(0xFF673AB7) else Color(
                    0xFF03A9F4
                )
                ) ){
                Text("上一篇")

            }
            Button(onClick = onNextClick,
                colors = ButtonDefaults.buttonColors(if (ThemeManager.isDarkTheme) Color(0xFF673AB7) else Color(
                    0xFF03A9F4
                )
                )){
                Text("下一篇")
            }
        }

        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    layoutParams = android.view.ViewGroup.LayoutParams(
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT
                    )
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
                    settings.javaScriptEnabled = true
                    loadUrl(currentUrl)
                }
            },
            update = { webView ->
                if (webView.url != currentUrl) {
                    webView.loadUrl(currentUrl)
                }
            },
            modifier = Modifier
                .fillMaxSize()
        )

    }

    LaunchedEffect(url) {
        currentUrl = url
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
        )
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
                        color = Color.Black,
                        modifier = Modifier.padding(top = 16.dp),
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        viewModel.commentsData?.data?.comments?.let { comments ->
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
                                            .clip(CircleShape)
                                    )
                                    Column(
                                        modifier = Modifier
                                            .padding(start = 16.dp)
                                            .weight(1f)
                                            .align(Alignment.CenterVertically)
                                    ) {
                                        Text(
                                            color = Color.Black,
                                            text = comment.author,
                                            style = TextStyle(
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                        Text(
                                            color = Color.Black,
                                            text = comment.content,
                                            style = TextStyle(fontSize = 14.sp),
                                            modifier = Modifier.padding(top = 8.dp)
                                        )
                                    }
                                }
                            }
                        } ?: run {
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
            Button(
                onClick = { onExpandChange(!isExpanded) },
                modifier = Modifier
                    .align(if (isExpanded) Alignment.BottomEnd else Alignment.Center)
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(if (ThemeManager.isDarkTheme) Color(
                    0xFF673AB7
                ) else Color(0xFF03A9F4)
                )
            ) {
                Text(text = if (isExpanded) "收缩评论" else "展开评论")
            }
        }
    }
}