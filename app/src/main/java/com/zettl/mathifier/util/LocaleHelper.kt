package com.zettl.mathifier.util

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import com.zettl.mathifier.data.datastore.LANGUAGE_DE
import com.zettl.mathifier.data.datastore.LANGUAGE_EN
import com.zettl.mathifier.data.datastore.LANGUAGE_SYSTEM
import java.util.Locale

object LocaleHelper {

    fun applyLocale(context: Context, languageCode: String): Context {
        if (languageCode == LANGUAGE_SYSTEM) return context
        val locale = when (languageCode) {
            LANGUAGE_DE -> Locale.GERMAN
            LANGUAGE_EN -> Locale.ENGLISH
            else -> Locale.getDefault()
        }
        val config = Configuration(context.resources.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocales(LocaleList(locale))
        } else {
            @Suppress("DEPRECATION")
            config.locale = locale
        }
        return context.createConfigurationContext(config)
    }
}
