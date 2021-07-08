package com.example.myapplication.utils

import androidx.biometric.BiometricPrompt

data class FingerprintResult(val result : BiometricPrompt.AuthenticationResult? = null, val errorString: String? = null)