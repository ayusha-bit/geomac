package io.silentsea.geomac.ui

import android.Manifest
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import io.silentsea.geomac.R
import io.silentsea.geomac.ui.components.Card
import io.silentsea.geomac.ui.components.ErrorSheet
import io.silentsea.geomac.ui.components.InputTextField
import io.silentsea.geomac.ui.components.WifiScanSheet
import io.silentsea.geomac.utils.AppPermission
import io.silentsea.geomac.utils.macString
import io.silentsea.geomac.utils.rememberAppPermissionState
import io.silentsea.geomac.utils.showToast
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Geomac() {
    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    val viewModel = viewModel { GeomacViewModel() }

    val snackbarHostState = remember { SnackbarHostState() }

    val searching by viewModel.searching.collectAsState()
    val lazyPagingItems = viewModel.pagingFlow.collectAsLazyPagingItems()

    val textFieldState = rememberTextFieldState()

    LaunchedEffect(textFieldState) {
        snapshotFlow { textFieldState.text }.collectLatest { input ->
            viewModel.setInput(input.toString())
        }
    }

    var isFocused by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var error by remember { mutableStateOf<Throwable?>(null) }

    var swiped by remember { mutableStateOf<Long?>(null) }

    val wifiManager = context.getSystemService(WifiManager::class.java)
    var isWifiScanSheetOpened by remember { mutableStateOf(false) }
    val wifiScanPermissions = rememberAppPermissionState(
        permissions = listOf(
            AppPermission(
                permission = Manifest.permission.ACCESS_FINE_LOCATION,
                description = stringResource(R.string.permission_description),
                isRequired = true
            )
        )
    )

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    enabled = isFocused
                ) {
                    keyboardController?.hide()
                    focusManager.clearFocus(true)
                },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                InputTextField(
                    textFieldState = textFieldState,
                    onSearch = {
                        coroutineScope.launch {
                            viewModel.search()
                        }
                    },
                    onFocusChanged = {
                        isFocused = it
                    }
                )

                IconButton(
                    onClick = {
                        if (wifiScanPermissions.allRequiredGranted()) {
                            @Suppress("DEPRECATION")
                            if (wifiManager.isWifiEnabled || (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && wifiManager.isScanAlwaysAvailable)) {
                                isWifiScanSheetOpened = true
                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                context.showToast(
                                    text = context.getString(R.string.turn_on_wifi)
                                )

                                context.startActivity(
                                    Intent(Settings.Panel.ACTION_WIFI)
                                )
                            } else {
                                @Suppress("DEPRECATION")
                                wifiManager.isWifiEnabled = true
                                isWifiScanSheetOpened = true
                            }
                        } else {
                            wifiScanPermissions.requestPermission()
                        }
                    }
                ) {
                    Icon(
                        painterResource(R.drawable.wifi_find_24px),
                        contentDescription = null
                    )
                }
            }

            HorizontalDivider()

            Crossfade(
                targetState = lazyPagingItems.loadState.refresh,
            ) { refreshState ->
                when (refreshState) {
                    is LoadState.Error -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(
                                8.dp,
                                Alignment.CenterVertically
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.error),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineSmall
                            )

                            TextButton(
                                onClick = {
                                    error = refreshState.error
                                }
                            ) {
                                Text(
                                    text = stringResource(R.string.show_stack_trace)
                                )
                            }
                        }
                    }

                    is LoadState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            LoadingIndicator()
                        }
                    }

                    is LoadState.NotLoading -> {
                        Crossfade(
                            targetState = lazyPagingItems.itemCount > 0
                        ) { isNotEmpty ->
                            if (isNotEmpty) {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    contentPadding = PaddingValues(16.dp),
                                ) {
                                    items(
                                        lazyPagingItems.itemCount,
                                        key = lazyPagingItems.itemKey { it.mac }
                                    ) { index ->
                                        lazyPagingItems[index]?.let { item ->
                                            Card(
                                                item = item,
                                                isSearching = searching.contains(item.mac),
                                                isSwiped = swiped == item.mac,
                                                onDelete = {
                                                    coroutineScope.launch {
                                                        viewModel.delete(item.mac)

                                                        val result = snackbarHostState
                                                            .showSnackbar(
                                                                message = context.getString(
                                                                    R.string.mac_deleted,
                                                                    item.mac.macString()
                                                                ),
                                                                actionLabel = context.getString(R.string.undo),
                                                                duration = SnackbarDuration.Short
                                                            )

                                                        if (result == SnackbarResult.ActionPerformed) {
                                                            viewModel.undo(item)
                                                        }
                                                    }
                                                },
                                                onUpdate = {
                                                    coroutineScope.launch {
                                                        viewModel.search(item.mac)
                                                    }
                                                },
                                                onSwipe = {
                                                    swiped = item.mac
                                                }
                                            )
                                        }
                                    }

                                    val appendState = lazyPagingItems.loadState.append

                                    if (appendState is LoadState.Error) {
                                        item {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .animateItem(),
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Text(
                                                    text = stringResource(R.string.error),
                                                    textAlign = TextAlign.Center,
                                                    style = MaterialTheme.typography.headlineSmall
                                                )

                                                TextButton(
                                                    onClick = {
                                                        error = appendState.error
                                                    }
                                                ) {
                                                    Text(
                                                        text = stringResource(R.string.show_stack_trace)
                                                    )
                                                }
                                            }
                                        }
                                    } else if (appendState is LoadState.Loading) {
                                        item {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .animateItem(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                LoadingIndicator()
                                            }
                                        }
                                    }
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = stringResource(R.string.empty),
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                }
                            }
                        }
                    }
                }
            }

            HorizontalDivider()
        }
    }

    BackHandler(
        enabled = isFocused
    ) {
        keyboardController?.hide()
        focusManager.clearFocus(true)
    }

    error?.let {
        ErrorSheet(
            error = it,
            onDismissRequest = {
                error = null
            }
        )
    }

    if (isWifiScanSheetOpened) {
        WifiScanSheet(
            onSearch = { mac ->
                textFieldState.setTextAndPlaceCursorAtEnd(mac.macString(""))

                coroutineScope.launch {
                    viewModel.search(mac)
                }
            },
            onDismissRequest = {
                isWifiScanSheetOpened = false
            }
        )
    }
}