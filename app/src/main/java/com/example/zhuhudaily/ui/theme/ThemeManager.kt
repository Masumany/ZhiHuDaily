package com.example.zhuhudaily

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf

object ThemeManager {
    var _isDarkTheme = mutableStateOf(false)//默认不是暗黑主题
    val isDarkTheme: Boolean by _isDarkTheme

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value//切换主题
    }
}

