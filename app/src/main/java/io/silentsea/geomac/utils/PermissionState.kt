package io.silentsea.geomac.utils


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import java.lang.ref.WeakReference


@SuppressLint("ComposableNaming")
@Composable
fun rememberAppPermissionState(permissions: List<AppPermission>): PermissionState {
    val context = LocalContext.current
    val activity = requireNotNull(LocalActivity.current)

    val permissionState = remember(permissions) {
        PermissionState(permissions)
    }

    permissionState.contextRef = WeakReference(context)

    permissions.forEach {
        if (!checkPermissionAddedInManifest(it, context)) {
            throw PermissionNotAddedException(it.permission)
        }
    }

    PermissionLifeCycleCheckEffect(
        permissionState = permissionState
    )

    permissionState.launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        when {
            isGranted -> {
                permissionState.next()
                permissionState.allRequiredGranted()
            }

            else -> handlePermissionDenial(permissionState, activity)
        }
    }

    permissionState.currentPermission?.let { permission ->
        when {
            permissionState.showRationalePopUp -> {
                ShowPopup(
                    message = permission.description,
                    onConfirm = {
                        permissionState.showRationalePopUp = false
                        permissionState.requestPermission()
                    }
                )
            }

            permissionState.showSettingsPopUp -> {
                ShowPopup(
                    message = permission.description,
                    onConfirm = {
                        permissionState.showSettingsPopUp = false
                        permissionState.resumedFromSettings = true
                        openAppSettings(context)
                    }
                )
            }
        }
    }

    return permissionState
}

private fun handlePermissionDenial(permissionState: PermissionState, activity: Activity) {
    permissionState.currentPermission?.let { currentPermission ->
        when {
            currentPermission.isRequired &&
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        currentPermission.permission
                    ) ->
                permissionState.showRationalePopUp = true

            currentPermission.isRequired ->
                permissionState.showSettingsPopUp = true

            else ->
                permissionState.next()
        }
    }
}

private fun openAppSettings(context: Context) {
    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        setData(Uri.fromParts("package", context.packageName, null))
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(this)
    }
}

class PermissionState(
    permissionList: List<AppPermission>,
) {
    lateinit var contextRef: WeakReference<Context>

    private val allPermissions = mutableStateListOf<AppPermission>()

    private var pendingPermissions = mutableStateListOf<AppPermission>()

    var currentPermission by mutableStateOf<AppPermission?>(null)

    var showRationalePopUp by mutableStateOf(false)
    var showSettingsPopUp by mutableStateOf(false)
    var resumedFromSettings by mutableStateOf(false)

    private var isRequiredPermissionGranted by mutableStateOf(false)

    var launcher: ManagedActivityResultLauncher<String, Boolean>? = null

    init {
        allPermissions.addAll(permissionList)
        pendingPermissions.addAll(permissionList)
    }

    fun allRequiredGranted(): Boolean {
        isRequiredPermissionGranted = allPermissions
            .filter { it.isRequired }
            .all { isGranted(it.permission) }

        return isRequiredPermissionGranted
    }

    fun isGranted(permission: String): Boolean {
        val context = requireNotNull(contextRef.get())

        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission() {
        if (pendingPermissions.isNotEmpty()) {
            currentPermission = pendingPermissions.first()

            currentPermission?.let { permission ->
                if (isGranted(permission.permission)) {
                    next()
                } else {
                    launcher?.launch(permission.permission)
                }
            }
        }
    }

    fun next() {
        if (pendingPermissions.isNotEmpty()) {
            pendingPermissions.removeAt(0)
        }

        requestPermission()
    }
}