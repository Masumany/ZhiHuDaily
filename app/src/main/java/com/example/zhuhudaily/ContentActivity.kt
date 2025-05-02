package com.example.zhuhudaily

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.zhuhudaily.ui.theme.ZhuHuDailyTheme

class ContentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZhuHuDailyTheme (darkTheme = ThemeManager.isDarkTheme){
                val url = intent.getStringExtra("url") ?: "" // 从Intent中获取URL
                Content(
                    modifier = Modifier.fillMaxSize()
                        .background(if (ThemeManager.isDarkTheme) Color.Black else Color.White),
                    url = url
                )
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
                    webViewClient = WebViewClient() // 确保在APP内打开网页而非浏览器
                    settings.javaScriptEnabled = true // 启用JS支持
                    loadUrl(url) // 加载URL
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun showing() {
    ZhuHuDailyTheme {
        Content(
            modifier = Modifier.fillMaxSize(),
            url = "https://www.example.com"
        )
    }
}