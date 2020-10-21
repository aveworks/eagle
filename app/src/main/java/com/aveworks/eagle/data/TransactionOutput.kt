package com.aveworks.eagle.data

import android.os.Parcelable

import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class TransactionOutput(
    @SerialName("addr") val address: String,
    @SerialName("value") val value: Long,
    @SerialName("xpub") val xpub: XPub? = null,
) : Parcelable{
    fun cryptoValue(): String = com.aveworks.eagle.utils.cryptoValue(value)
}