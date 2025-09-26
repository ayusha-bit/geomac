package io.silentsea.geomac

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import io.silentsea.geomac.ui.Geomac
import io.silentsea.geomac.ui.theme.GeomacTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(0, 0),
            navigationBarStyle = SystemBarStyle.auto(0, 0)
        )

        super.onCreate(savedInstanceState)

        setContent {
            GeomacTheme {
                Geomac()
            }
        }
    }
}