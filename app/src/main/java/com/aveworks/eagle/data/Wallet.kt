package com.aveworks.eagle.data

import com.aveworks.eagle.utils.cryptoValue
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Wallet(
    @field:JsonProperty("final_balance") val finalBalance: Long
) {
    fun cryptoValue(): String = cryptoValue(finalBalance)
}