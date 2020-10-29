package com.aveworks.common.data

import com.aveworks.common.Parcelable
import com.aveworks.common.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class XPub(
    @SerialName("m") val hash: String,
    @SerialName("path") val amount: String,
) : Parcelable
