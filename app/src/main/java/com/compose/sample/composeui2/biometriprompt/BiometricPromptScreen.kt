package com.compose.sample.composeui2.biometriprompt

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun BiometricPromptScreen() {
    val activity = LocalContext.current as AppCompatActivity
    val promptManager = remember {
        BiometricPromptManager(activity)
    }

    val enrollLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            println("Activity Result: $it")
        }
    )

    val biometricResult by promptManager.resultState.collectAsState()

    LaunchedEffect(biometricResult) {
        if (biometricResult is BiometricResult.AuthenticationNotSet) {
            if (Build.VERSION.SDK_INT >= 30) {
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
                    )
                }
                enrollLauncher.launch(enrollIntent)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                promptManager.showBiometricPrompt("Sample prompt", "sample prompt description")
            },
        ) {
            Text(text = "Authenticate")
        }
        Text(
            text = when (biometricResult) {
                is BiometricResult.AuthenticationError -> {
                    (biometricResult as BiometricResult.AuthenticationError).error
                }

                BiometricResult.AuthenticationFailed -> "Authentication Failed"
                BiometricResult.AuthenticationNotSet -> "Authentication not set"
                BiometricResult.AuthenticationSuccess -> "Authentication Success"
                BiometricResult.FeatureUnavailable -> "Feature Unavailable"
                BiometricResult.HardwareUnavailable -> "Hardware Unavailable"
            }
        )

    }
}