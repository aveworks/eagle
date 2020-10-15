package com.aveworks.eagle.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import kotlin.math.abs

@JsonIgnoreProperties(ignoreUnknown=true)
data class Wallet(
    @field:JsonProperty("final_balance") val finalBalance: Int
){
    fun cryptoValue(): String = String.format("%.8f ${Transaction.CRYPTO_SYMBOL}" , abs(finalBalance) / Transaction.SHATOSHI_DIVIDER)
}