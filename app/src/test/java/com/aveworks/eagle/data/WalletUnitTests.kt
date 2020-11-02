package com.aveworks.eagle.data

import com.aveworks.common.data.Wallet
import com.aveworks.eagle.TestUtils
import org.junit.Assert.*
import org.junit.Test
import org.junit.BeforeClass


// TODO move tests to common
class WalletUnitTests {
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