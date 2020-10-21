package com.aveworks.eagle.data

import com.aveworks.eagle.utils.cryptoValue
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Wallet(
    @SerialName("final_balance") val finalBalance: Long
) {
    fun cryptoValue(): String = cryptoValue(finalBalance)
}