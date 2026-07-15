package com.alessandro.djsimulator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.alessandro.djsimulator.ui.DjSimulatorApp
import com.alessandro.djsimulator.ui.DjTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { DjTheme { DjSimulatorApp() } }
    }
}
