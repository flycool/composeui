package com.compose.sample.composeui.permission

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale


@Composable
fun OptionalSinglePermission() {
    OptionalSinglePermission(android.Manifest.permission.CALL_PHONE)
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun OptionalSinglePermission(permission: String) {

    var statusText by remember { mutableStateOf("") }
    val permissionState = rememberPermissionState(permission = permission)

    var launchPermissionDialog by remember { mutableStateOf(true) }
    var showRationale by remember { mutableStateOf(true) }

    val status = permissionState.status

    if (status.isGranted) {
        statusText = "Granted"
    } else if (status.shouldShowRationale) {
        statusText = "Denied"
        if (showRationale) {
            OptionalRationalPermissionDialog(permission,
                dismissCallback = { showRationale = false })
        }
    } else {
        statusText = "N/A"
        if (launchPermissionDialog) {
            OptionalLaunchPermissionDialog(
                permission = permission,
                launchPermission = {
                    permissionState.launchPermissionRequest()
                },
                dismissCallback = { launchPermissionDialog = false }
            )
        }

//        SideEffect {
//            permissionState.launchPermissionRequest()
//        }

    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Optional Permission status: $statusText")
    }

}

@Composable
fun OptionalLaunchPermissionDialog(
    permission: String,
    launchPermission: () -> Unit,
    dismissCallback: () -> Unit
) {
    val context = LocalContext.current
    val permissionLabel = stringResource(
        context.packageManager.getPermissionInfo(permission, 0).labelRes
    )

    AlertDialog(onDismissRequest = { dismissCallback() },
        title = { Text(text = "Permission Required") },
        text = { Text(text = permissionLabel) },
        confirmButton = {
            Button(onClick = { launchPermission() }) {
                Text(text = "Launch")
            }
        },
        dismissButton = {
            Button(onClick = { dismissCallback() }) {
                Text(text = "Cancel")
            }
        })
}

@Composable
fun OptionalRationalPermissionDialog(
    permission: String, dismissCallback: () -> Unit
) {

    val context = LocalContext.current
    val permissionLabel = stringResource(
        context.packageManager.getPermissionInfo(permission, 0).labelRes
    )

    AlertDialog(onDismissRequest = { dismissCallback() },
        title = { Text(text = "Permission Required") },
        text = { Text(text = permissionLabel) },
        confirmButton = {
            Button(onClick = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                ContextCompat.startActivity(context, intent, null)
            }) {
                Text(text = "Go to settings")
            }
        },
        dismissButton = {
            Button(onClick = { dismissCallback() }) {
                Text(text = "Cancel")
            }
        })
}
