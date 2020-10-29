package com.aveworks.common.utils

import com.aveworks.common.data.Transaction
import kotlin.math.abs

const val SHATOSHI_DIVIDER = 1e8
const val CRYPTO_SYMBOL = "BTC"
const val FIAT_SYMBOL = "$"

expect fun cryptoValue(value: Long): String
expect fun fiatValue(value: Long): String

expect fun transactionDate(transaction: Transaction, full: Boolean): String


/**
 * Enabling the following extension can make all Long types having crypto/fiat values string formatting
 * but can lead unfair and probably wrong usage
 */
//fun Long.cryptoValue(): String = String.format("%.8f $CRYPTO_SYMBOL", abs(this) / SHATOSHI_DIVIDER)
//fun Long.fiatValue(): String = String.format("$FIAT_SYMBOL%.2f", abs(this) / 100.0)


