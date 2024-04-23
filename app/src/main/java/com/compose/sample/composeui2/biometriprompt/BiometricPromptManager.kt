package com.compose.sample.composeui2.biometriprompt

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class BiometricPromptManager(private val activity: AppCompatActivity) {

    private var _resultState =
        MutableStateFlow<BiometricResult>(BiometricResult.AuthenticationNotSet)
    val resultState = _resultState.asStateFlow()

    fun showBiometricPrompt(title: String, description: String) {
        val manager = BiometricManager.from(activity)
        val authenticators = if (Build.VERSION.SDK_INT >= 30) {
            BIOMETRIC_STRONG or DEVICE_CREDENTIAL
        } else BIOMETRIC_STRONG

        val promptInfo = PromptInfo.Builder().apply {
            setTitle(title)
            setDescription(description)
            setAllowedAuthenticators(authenticators)

            if (Build.VERSION.SDK_INT < 30) {
                setNegativeButtonText("Cancel")
            }
        }

        when (manager.canAuthenticate(authenticators)) {
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                _resultState.value = BiometricResult.HardwareUnavailable
                return
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                _resultState.value = BiometricResult.FeatureUnavailable
                return
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                _resultState.value = BiometricResult.AuthenticationNotSet
                return
            }

            else -> Unit
        }

        val prompt = BiometricPrompt(activity, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                _resultState.value = BiometricResult.AuthenticationError(errString.toString())
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                _resultState.value = BiometricResult.AuthenticationSuccess
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                _resultState.value = BiometricResult.AuthenticationFailed
            }
        })

        prompt.authenticate(promptInfo.build())
    }
}

sealed interface BiometricResult {
    data object HardwareUnavailable : BiometricResult
    data object FeatureUnavailable : BiometricResult
    data class AuthenticationError(val error: String) : BiometricResult
    data object AuthenticationFailed : BiometricResult
    data object AuthenticationSuccess : BiometricResult
    data object AuthenticationNotSet : BiometricResult

}