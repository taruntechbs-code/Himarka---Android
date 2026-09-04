package com.example.himarka.core.localization

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LanguageManager {
    fun setLocale(context: Context, language: AppLanguage): Context {
        val locale = Locale.forLanguageTag(language.code)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }
}
