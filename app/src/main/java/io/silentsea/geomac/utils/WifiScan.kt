package io.silentsea.geomac.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun rememberWifiScanResults(
    intervalMillis: Long = 10_000L
): List<ScanResult> {
    val context = LocalContext.current

    val wifiManager = context.getSystemService(WifiManager::class.java)

    var scanResults by remember { mutableStateOf(emptyList<ScanResult>()) }

    DisposableEffect(wifiManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val callback = object : WifiManager.ScanResultsCallback() {
                override fun onScanResultsAvailable() {
                    @Suppress("MissingPermission")
                    scanResults = wifiManager.scanResults.sortedByDescending { it.level }
                }
            }

            wifiManager.registerScanResultsCallback(context.mainExecutor, callback)

            onDispose { runCatching { wifiManager.unregisterScanResultsCallback(callback) } }
        } else {
            val receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    if (intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)) {
                        @Suppress("MissingPermission")
                        scanResults = wifiManager.scanResults.sortedByDescending { it.level }
                    }
                }
            }

            val filter = IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED)
            } else {
                context.registerReceiver(receiver, filter)
            }

            onDispose { runCatching { context.unregisterReceiver(receiver) } }
        }
    }

    LaunchedEffect(wifiManager, intervalMillis) {
        while (isActive) {
            @Suppress("DEPRECATION")
            wifiManager.startScan()
            delay(intervalMillis)
        }
    }

    return scanResults
}
