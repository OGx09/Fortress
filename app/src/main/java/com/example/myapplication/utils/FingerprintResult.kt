package com.example.myapplication.utils

import androidx.biometric.BiometricPrompt

data class FingerprintResult(val errorString: String? = null,
                             val cryptoObject: BiometricPrompt.CryptoObject? = null)