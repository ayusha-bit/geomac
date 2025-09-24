package io.silentsea.geomac.di

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpRedirect
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.silentsea.geomac.BuildConfig
import org.koin.dsl.module

val networkModule = module {
    single {
        HttpClient(CIO) {
            followRedirects = false
            expectSuccess = true

            install(HttpRedirect) {
                allowHttpsDowngrade = true
                checkHttpMethod = false
            }

            if (BuildConfig.DEBUG) {
                install(Logging) {
                    logger = object : Logger {
                        override fun log(message: String) {
                            Log.d("geomac", message)
                        }
                    }
                    level = LogLevel.ALL
                }
            }
        }
    }
}