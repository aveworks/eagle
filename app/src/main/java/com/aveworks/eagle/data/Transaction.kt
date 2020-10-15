package com.aveworks.eagle.data

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

@Parcelize
@JsonIgnoreProperties(ignoreUnknown = true)
data class Transaction(
    @field:JsonProperty("hash") val hash: String,
    @field:JsonProperty("result") val amount: Long,
    @field:JsonProperty("fee") val fee: Long,
    @field:JsonProperty("time") val timestamp: Long,
    @field:JsonProperty("block_height") val blockHeight: Long?,
    @field:JsonProperty("out") val outputs: List<TransactionOutput> = listOf(),

    var fiatAmount: Long?,
) : Parcelable {

    @IgnoredOnParcel
    val address: String? by lazy {
        var addr: String? = null
        for (output in outputs) {
            if (isReceive() || output.xpub == null) {
                return@lazy output.address
            } else {
                addr = output.address
            }
        }
        addr
    }

    // Get all output addresses excluding our address (eg. change)
//    val addresses : List<String> = outputs.filter { it.xpub == null }.map { it.address }
//
//    val addressesString : String by lazy {
//        addressList.joinToString(", ")
//    }

    fun isSent(): Boolean {
        return amount < 0
    }

    fun isReceive(): Boolean = !isSent()

    fun cryptoValue(): String = String.format("%.8f $CRYPTO_SYMBOL", abs(amount) / SHATOSHI_DIVIDER)

    fun feeValue(): String = String.format("%.8f $CRYPTO_SYMBOL", abs(fee) / SHATOSHI_DIVIDER)

    fun fiatValue(): String? =
        fiatAmount?.let { String.format("$FIAT_SYMBOL%.2f", abs(it) / 100.0) }

    fun date(full: Boolean): String? =
        (if (full) dateFormatFull else dateFormatShort).let { it.format(Date(timestamp * 1000)) }

    companion object {
        internal const val SHATOSHI_DIVIDER = 1e8
        internal const val CRYPTO_SYMBOL = "BTC"
        private const val FIAT_SYMBOL = "$"

        private val dateFormatFull = SimpleDateFormat("MMM d, yyyy HH:mm:ss", Locale.US)
        private val dateFormatShort = SimpleDateFormat("MMM d, yyyy", Locale.US)
    }
}