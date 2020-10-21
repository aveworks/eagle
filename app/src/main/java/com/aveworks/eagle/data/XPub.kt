package com.aveworks.eagle.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class XPub(
    @SerialName("m") val hash: String,
    @SerialName("path") val amount: String,
) : Parcelable
