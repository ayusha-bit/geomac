package io.silentsea.geomac

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import io.silentsea.geomac.ui.Geomac
import io.silentsea.geomac.ui.theme.GeomacTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        Color.Transparent.toArgb().also {
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.auto(it, it),
                navigationBarStyle = SystemBarStyle.auto(it, it)
            )
        }

        super.onCreate(savedInstanceState)

        setContent {
            GeomacTheme {
                Geomac()
            }
        }
    }
}