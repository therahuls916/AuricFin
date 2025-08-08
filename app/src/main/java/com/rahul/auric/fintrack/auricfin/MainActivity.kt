package com.rahul.auric.fintrack.auricfin // Make sure package name is correct

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rahul.auric.fintrack.auricfin.ui.screens.MainScreen
import com.rahul.auric.fintrack.auricfin.ui.theme.AuricFinTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AuricFinTheme {
                // The MainScreen is the single source of truth for your app's UI.
                // All other screens are navigated to from within it.
                MainScreen()
            }
        }
    }
}