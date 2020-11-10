package com.aveworks.eagle.repositories

import com.aveworks.common.data.MultiAddressResponse
import com.aveworks.eagle.Analytics
import com.aveworks.eagle.api.BlockchainService
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionsRepository @Inject constructor(
    private val analytics: Analytics,
    private val service: BlockchainService,
) {
    fun getTransactions(xpub: String, offset: Int = 0): Single<MultiAddressResponse> {
        return service
            .multiAddressObservable(xpub, offset = offset)
            .doOnError {
                analytics.trackException(it)
            }
            .map { it ->
                it.also { response ->
                    val conv = response.info.local["conversion"]?.jsonPrimitive?.doubleOrNull

                    if (conv is Double) {
                        response.txs.map { tx ->
                            tx.also {
                                it.fiatAmount = (it.amount / 1e8 * conv * 100).toLong()
                            }
                        }
                    }
                }
            }
    }
}