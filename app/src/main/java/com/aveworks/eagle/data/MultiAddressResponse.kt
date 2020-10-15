package com.aveworks.eagle.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown=true)
data class MultiAddressResponse (
    @field:JsonProperty("wallet") val wallet: Wallet,
    @field:JsonProperty("txs") val txs: List<Transaction>,
    @field:JsonProperty("info") val info: Info,
)