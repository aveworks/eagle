package com.aveworks.common.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MultiAddressResponse (
    @SerialName("wallet") val wallet: Wallet,
    @SerialName("txs") val txs: List<Transaction>,
    @SerialName("info") val info: Info,
)