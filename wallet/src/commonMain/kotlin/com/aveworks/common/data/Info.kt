package com.aveworks.common.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class Info(
    @SerialName("symbol_local") val local: Map<String, JsonElement>,
)