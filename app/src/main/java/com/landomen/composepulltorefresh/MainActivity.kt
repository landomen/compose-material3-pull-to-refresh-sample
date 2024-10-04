package com.landomen.composepulltorefresh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.landomen.composepulltorefresh.ui.home.HomeScreen
import com.landomen.composepulltorefresh.ui.theme.ComposePullToRefreshTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposePullToRefreshTheme {
                HomeScreen()
            }
        }
    }
}