package com.aveworks.eagle.data

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonIgnoreProperties(ignoreUnknown=true)
data class XPub(
    @field:JsonProperty("m") val hash: String,
    @field:JsonProperty("path") val amount: String,
) : Parcelable
