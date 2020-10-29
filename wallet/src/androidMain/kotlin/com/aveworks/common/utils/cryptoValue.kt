package com.aveworks.common.utils

import com.aveworks.common.data.Transaction
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

private val dateFormatFull = SimpleDateFormat("MMM d, yyyy HH:mm:ss", Locale.US)
private val dateFormatShort = SimpleDateFormat("MMM d, yyyy", Locale.US)

actual fun cryptoValue(value: Long): String =
    String.format("%.8f $CRYPTO_SYMBOL", abs(value) / SHATOSHI_DIVIDER)

actual fun fiatValue(value: Long): String = String.format("$FIAT_SYMBOL%.2f", abs(value) / 100.0)

actual fun transactionDate(transaction: Transaction, full: Boolean): String {
    return (if (full) dateFormatFull else dateFormatShort).let {
        it.format(Date(transaction.timestamp * 1000))
    }
}