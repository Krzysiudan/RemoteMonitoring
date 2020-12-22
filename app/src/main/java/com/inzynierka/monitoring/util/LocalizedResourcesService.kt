package com.inzynierka.monitoring.util

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import java.util.*

object LocalizedResourcesService {

    fun getLocalizedResources(context: Context, desiredLocale: Locale): Resources{
        var conf : Configuration = context.resources.configuration
        conf = Configuration(conf)
        conf.setLocale(desiredLocale)
        val localizedContext = context.createConfigurationContext(conf)
        return localizedContext.resources
    }
}