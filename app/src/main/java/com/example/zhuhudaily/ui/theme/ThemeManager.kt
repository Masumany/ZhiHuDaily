package com.example.zhuhudaily

import androidx.compose.runtime.Composable

object ThemeManager {
    var isDarkTheme = false//默认不是暗黑主题
        private set

    fun toggleTheme() {
        isDarkTheme = !isDarkTheme//切换主题
    }
}

