package com.zettl.mathifier

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.zettl.mathifier.ui.MathifierApp
import com.zettl.mathifier.ui.theme.MathifierTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val app = application as MathifierApplication
        setContent {
            MathifierTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MathifierApp(container = app.appContainer)
                }
            }
        }
    }
}
