package com.aveworks.eagle.utils

import kotlin.math.abs

const val SHATOSHI_DIVIDER = 1e8
const val CRYPTO_SYMBOL = "BTC"
const val FIAT_SYMBOL = "$"

fun cryptoValue(value: Long): String =
    String.format("%.8f $CRYPTO_SYMBOL", abs(value) / SHATOSHI_DIVIDER)

fun fiatValue(value: Long): String = String.format("$FIAT_SYMBOL%.2f", abs(value) / 100.0)

/**
 * Enabling the following extension can make all Long types having crypto/fiat values string formatting
 * but can lead unfair and probably wrong usage
 */
//fun Long.cryptoValue(): String = String.format("%.8f $CRYPTO_SYMBOL", abs(this) / SHATOSHI_DIVIDER)
//fun Long.fiatValue(): String = String.format("$FIAT_SYMBOL%.2f", abs(this) / 100.0)