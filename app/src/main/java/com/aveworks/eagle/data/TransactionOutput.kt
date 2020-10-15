package com.aveworks.eagle.data

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonIgnoreProperties(ignoreUnknown=true)
data class TransactionOutput(
    @field:JsonProperty("addr") val address: String,
    @field:JsonProperty("value") val value: Long,
    @field:JsonProperty("xpub") val xpub: XPub?,
) : Parcelable{
    fun cryptoValue(): String = com.aveworks.eagle.utils.cryptoValue(value)
}