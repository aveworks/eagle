package com.aveworks.eagle

import com.aveworks.eagle.data.Info
import com.aveworks.eagle.data.MultiAddressResponse
import com.aveworks.eagle.data.Transaction
import com.aveworks.eagle.data.Wallet
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.double
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class DataTest {
    private lateinit var wallet: Wallet
    private lateinit var info: Info
    private lateinit var txs: List<Transaction>

    @Before
    fun setup() {
        val response = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }.decodeFromString<MultiAddressResponse>(TestUtils.readFromFile("multiaddr.json"))

        wallet = response.wallet
        info = response.info
        txs = response.txs
    }

    @Test
    fun testWallet_balance() {
        assertEquals(8549, wallet.finalBalance)
    }

    @Test
    fun testInfo_conversion() {
        assertEquals(8769.98350366, info.local["conversion"]?.jsonPrimitive?.doubleOrNull)
    }

    @Test
    fun testTransaction_list() {
        assertEquals(50, txs.size)
        assertNotNull(txs[0])
        assertNotNull(txs[49])
    }

    @Test
    fun testTransaction_send() {
        val send = txs[0]

        assert(send.amount < 0)
        assertEquals(-612687, send.amount)
        assertEquals(36881, send.fee)
        assertEquals("cbc06203f949804a512290ade05dcab35cf30c16b43bb0ede6f5074f1f8c3b9e", send.hash)

        // Pending tx
        assertTrue(send.isPending())

        assertEquals("0.00612687 BTC", send.cryptoValue())
        assertEquals("0.00036881 BTC", send.feeValue())
        assertEquals("Jan 11, 2019", send.date(false))
        assertEquals("Jan 11, 2019 18:13:10", send.date(true))
        assertTrue(send.isSent())
        assertFalse(send.isReceive())
    }

    @Test
    fun testTransaction_receive() {
        val receive = txs[1]

        assert(receive.amount > 0)
        assertEquals(559182, receive.amount)
        assertTrue(receive.isReceive())
        assertFalse(receive.isSent())
        assertFalse(receive.isPending())

        assertEquals("0.00559182 BTC", receive.cryptoValue())
    }

}