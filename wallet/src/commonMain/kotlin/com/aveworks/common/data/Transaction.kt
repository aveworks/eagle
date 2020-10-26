package com.aveworks.common.data

import com.aveworks.common.IgnoredOnParcel
import com.aveworks.common.Parcelable
import com.aveworks.common.Parcelize
import com.aveworks.common.utils.transactionDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import com.aveworks.common.utils.*

@Parcelize
@Serializable
data class Transaction(
    @SerialName("hash") val hash: String,
    @SerialName("result") val amount: Long,
    @SerialName("fee") val fee: Long,
    @SerialName("time") val timestamp: Long,
    @SerialName("block_height") val blockHeight: Long? = null,
    @SerialName("out") val outputs: List<TransactionOutput> = listOf(),

    @Transient
    var fiatAmount: Long? = null,
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
    @IgnoredOnParcel
    val addresses : List<String> = outputs.filter { it.xpub == null }.map { it.address }

    /**
     * Get the first non change output if exists
     */
    @IgnoredOnParcel
    val output: TransactionOutput? by lazy{
        for (output in outputs) {
            if (isReceive() || output.xpub == null) {
                return@lazy output
            }
        }

        null
    }

    fun isPending(): Boolean = blockHeight == null

    fun isSent(): Boolean {
        return amount < 0
    }

    fun isReceive(): Boolean = !isSent()

    fun cryptoValue(): String = cryptoValue(amount)

    fun feeValue(): String = cryptoValue(fee)

    fun fiatValue(): String? = fiatAmount?.let { fiatValue(it) }

    fun date(full: Boolean): String? = transactionDate(this, full)
}