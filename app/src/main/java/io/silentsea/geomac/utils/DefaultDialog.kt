package io.silentsea.geomac.utils

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.silentsea.geomac.R

@Composable
fun ShowPopup(
    message: String,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(
                text = stringResource(R.string.permission)
            )
        },
        text = {
            Text(
                text = message
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm
            ) {
                Text(
                    text = stringResource(R.string.ok)
                )
            }
        }
    )
}