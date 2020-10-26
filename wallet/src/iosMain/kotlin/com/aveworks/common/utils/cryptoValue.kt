package com.aveworks.common.utils

import com.aveworks.common.data.Transaction
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSTimeInterval
import platform.Foundation.dateWithTimeIntervalSince1970

import kotlin.math.abs

private val dateFormatFull =  NSDateFormatter().apply {
    dateFormat = "MMM d, yyyy HH:mm:ss"
}

private val dateFormatShort =  NSDateFormatter().apply {
    dateFormat = "MMM d, yyyy"
}

actual fun cryptoValue(value: Long): String = "0"
//    String.format("%.8f $CRYPTO_SYMBOL", abs(value) / SHATOSHI_DIVIDER)

actual fun fiatValue(value: Long): String = "0 fiat"//String.format("$FIAT_SYMBOL%.2f", abs(value) / 100.0)

actual fun transactionDate(transaction: Transaction, full: Boolean): String {
    return (if (full) dateFormatFull else dateFormatShort).let {
        it.stringFromDate(NSDate.dateWithTimeIntervalSince1970(transaction.timestamp * 1000.0))
    }
}