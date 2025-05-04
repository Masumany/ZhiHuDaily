package com.example.zhuhudaily.View

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zhuhudaily.R
import com.example.zhuhudaily.ThemeManager
import com.example.zhuhudaily.ui.theme.ZhuHuDailyTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZhuHuDailyTheme(darkTheme = ThemeManager.isDarkTheme) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Login(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    @SuppressLint("UnrememberedMutableState")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Login(
        modifier: Modifier
    ) {
        val context = LocalContext.current
        var isChecked by remember { mutableStateOf(false) }

        // 使用 LaunchedEffect 监听主题状态变化，当主题状态改变时，UI 会自动更新
        LaunchedEffect(ThemeManager.isDarkTheme) {
            // 这里不需要额外操作，只要主题状态变化，就会触发 LaunchedEffect，进而触发 UI 重新组合
        }

        Column(modifier = modifier
            .background(if (ThemeManager.isDarkTheme) Color(0xFF1A1A1A) else Color.White)) {
            Image(
                painter = painterResource(if (ThemeManager.isDarkTheme) R.drawable.lightback else R.drawable.back),
                contentDescription = null,
                alignment = Alignment.TopStart,
                modifier = Modifier
                    .width(30.dp)
                    .padding(top = 20.dp)
                    .height(30.dp)
                    .size(30.dp)
                    .clickable {
                        onBackPressed()
                    }
            )
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (ThemeManager.isDarkTheme) Color(0xFF1A1A1A) else Color.White),
                title = {
                    Text(
                        text = "登录知乎日报",
                        style = TextStyle(
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (ThemeManager.isDarkTheme) Color.White else Color.Black
                        )
                    )
                }
            )
            Text(
                text = "请选择登陆方式",
                modifier = Modifier.fillMaxWidth()
                    .background(if (ThemeManager.isDarkTheme) Color(0xFF1A1A1A) else Color.White),
                style = TextStyle(
                    color = if (ThemeManager.isDarkTheme) Color.White else Color.Black,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            )
            Row(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .align(Alignment.CenterHorizontally)
                    .width(200.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.zhihu),
                    contentDescription = "知乎",
                    modifier = Modifier
                        .weight(1f)
                        .size(50.dp)
                        .clickable {
                            val url = "https://www.zhihu.com/signin"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        }
                )
                Image(
                    contentDescription = "微博",
                    painter = painterResource(R.drawable.weibo),
                    modifier = Modifier
                        .weight(1f)
                        .size(50.dp)
                        .clickable {
                            val url = "https://passport.weibo.cn/signin/login"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        }
                )
                Image(
                    contentDescription = "微信",
                    painter = painterResource(R.drawable.weixin),
                    modifier = Modifier
                        .weight(1f)
                        .size(50.dp)
                        .clickable {
                            val url = "https://wx.qq.com/"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        }
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { checked ->
                            isChecked = checked
                        },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Text(
                        text = "我已阅读并同意",
                        modifier = Modifier.align(Alignment.CenterVertically),
                        color = if (ThemeManager.isDarkTheme) Color.White else Color.Black
                    )
                    Text(
                        text = "《知乎日报用户协议》",
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .clickable {
                                val url = "https://www.zhihu.com/term/privacy?from=zhihuDaily"
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(intent)
                            },
                        style = TextStyle(
                            color = if (ThemeManager.isDarkTheme) Color(0xFFBB86FC) else Color(0xFF6200EE)
                        )
                    )
                }
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Column(
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(bottom = 20.dp)
                    ) {
                        Row {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(horizontal = 50.dp)
                            ) {
                                Image(
                                    painter = painterResource(if (ThemeManager.isDarkTheme) R.drawable.sun else R.drawable.moon),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clickable {
                                            ThemeManager.toggleTheme()
                                        }
                                )
                                Text(
                                    text = if (ThemeManager.isDarkTheme) "切换到亮色模式" else "切换到暗色模式",
                                    style = TextStyle(fontSize = 14.sp),
                                    modifier = Modifier.padding(top = 5.dp),
                                    color = if (ThemeManager.isDarkTheme) Color.White else Color.Black
                                )
                            }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(horizontal = 10.dp)
                            ) {
                                Image(
                                    painter = painterResource(if (ThemeManager.isDarkTheme) R.drawable.lightsetting else R.drawable.change),
                                    contentDescription = null,
                                    modifier = Modifier.size(50.dp)
                                )
                                Text(
                                    text = "设置",
                                    style = TextStyle(fontSize = 14.sp),
                                    modifier = Modifier.padding(top = 5.dp)
                                        .clickable {
                                            val url = "https://www.zhihu.com/signin"
                                            Toast.makeText(context, "请先登录知乎", Toast.LENGTH_SHORT).show()
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                            context.startActivity(intent)
                                        },
                                    color = if (ThemeManager.isDarkTheme) Color.White else Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun showing() {
        ZhuHuDailyTheme {
            Login(
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}