package com.example.zhuhudaily

import androidx.compose.runtime.Composable

object ThemeManager {
    var isDarkTheme = false
        private set

    fun toggleTheme() {
        isDarkTheme = !isDarkTheme
    }
}

