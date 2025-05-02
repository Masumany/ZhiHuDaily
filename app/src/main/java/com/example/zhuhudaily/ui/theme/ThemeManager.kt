package com.example.zhuhudaily

object ThemeManager {
    var isDarkTheme = false
        private set

    fun toggleTheme() {
        isDarkTheme = !isDarkTheme
    }
}