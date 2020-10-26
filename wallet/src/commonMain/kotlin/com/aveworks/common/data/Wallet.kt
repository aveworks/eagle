package com.aveworks.common.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.aveworks.common.utils.*


@Serializable
data class Wallet(
    @SerialName("final_balance") val finalBalance: Long
) {
    fun cryptoValue(): String = cryptoValue(finalBalance)
}