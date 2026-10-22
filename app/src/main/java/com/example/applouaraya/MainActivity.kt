package com.example.applouaraya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.applouaraya.navigation.AppNavigation
import com.example.applouaraya.ui.theme.LouArayaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { LouArayaTheme { AppNavigation() } }
    }
}
