package com.aveworks.common.data

import com.aveworks.common.Parcelable
import com.aveworks.common.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class TransactionOutput(
    @SerialName("addr") val address: String,
    @SerialName("value") val value: Long,
    @SerialName("xpub") val xpub: XPub? = null,
) : Parcelable{
    fun cryptoValue(): String = com.aveworks.common.utils.cryptoValue(value)
}