package com.aveworks.eagle

import com.aveworks.common.data.Info
import com.aveworks.common.data.MultiAddressResponse
import com.aveworks.common.data.Transaction
import com.aveworks.common.data.Wallet
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.double
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Assert.*
import org.junit.Test
import org.junit.BeforeClass


// TODO move tests to common
class WalletTest {
    companion object{
        private lateinit var wallet: Wallet

        @BeforeClass
        @JvmStatic
        fun setup() {
            wallet = TestUtils.getMultiAddressResponse().wallet
        }
    }

    @Test
    fun testWallet_balance() {
        assertEquals(8549, wallet.finalBalance)
    }
}