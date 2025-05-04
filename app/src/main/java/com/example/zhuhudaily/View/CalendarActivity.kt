package com.example.zhuhudaily.View

import com.example.zhuhudaily.ViewModel.CalendarViewModel
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zhuhudaily.R
import com.example.zhuhudaily.ThemeManager
import com.example.zhuhudaily.ui.theme.ZhuHuDailyTheme
import java.time.DayOfWeek
import java.time.format.DateTimeFormatter

class CalendarActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZhuHuDailyTheme(darkTheme = ThemeManager.isDarkTheme) {
                val viewModel: CalendarViewModel = viewModel()
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(if (ThemeManager.isDarkTheme) Color.Black else Color.White)
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ManualCalendarExample(viewModel)
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ManualCalendarExample(viewModel: CalendarViewModel) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { viewModel.previousMonth() }
            , modifier = Modifier.background(Color.Transparent)) {
                Text(text = "上一月")
            }
            Text(
                text = viewModel.currentMonth.format(DateTimeFormatter.ofPattern("yyyy 年 MM 月")),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Button(onClick = { viewModel.nextMonth() }) {
                Text(text = "下一月")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DayOfWeek.values().forEach { dayOfWeek ->
                Text(
                    text = dayOfWeek.name.take(3),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        val firstDayOfMonth = viewModel.currentMonth.atDay(1)
        val firstDayOfWeek = firstDayOfMonth.dayOfWeek
        val daysInMonth = viewModel.currentMonth.lengthOfMonth()

        var dayCounter = 1
        while (dayCounter <= daysInMonth) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (i in 1..7) {
                    if (dayCounter == 1 && i < firstDayOfWeek.value) {
                        Text(text = "")
                    } else if (dayCounter <= daysInMonth) {
                        Text(text = dayCounter.toString())
                        dayCounter++
                    } else {
                        Text(text = "")
                    }
                }
            }
        }
    }
}