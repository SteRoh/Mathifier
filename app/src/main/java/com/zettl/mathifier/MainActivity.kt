package com.zettl.mathifier

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.zettl.mathifier.ui.MathifierApp
import com.zettl.mathifier.data.datastore.LANGUAGE_SYSTEM
import com.zettl.mathifier.ui.theme.MathifierTheme
import com.zettl.mathifier.util.LocaleHelper

class MainActivity : ComponentActivity() {
    override fun attachBaseContext(newBase: Context) {
        val lang = (newBase.applicationContext as? MathifierApplication)
            ?.appContainer?.preferencesDataSource?.getLanguageBlocking() ?: LANGUAGE_SYSTEM
        super.attachBaseContext(LocaleHelper.applyLocale(newBase, lang))
    }

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
