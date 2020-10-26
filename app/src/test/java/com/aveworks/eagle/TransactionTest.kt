package com.aveworks.eagle

import com.aveworks.common.data.MultiAddressResponse
import com.aveworks.common.data.Transaction
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.BeforeClass
import org.junit.Test


// TODO move tests to common
class TransactionTest {

    companion object{
        private lateinit var pending: Transaction
        private lateinit var send: Transaction
        private lateinit var receive: Transaction


        @BeforeClass
        @JvmStatic
        fun setup() {
            val response = TestUtils.getMultiAddressResponse()

            send = response.txs[0]
            receive = response.txs[1]

            pending = send
        }
    }

    @Test
    fun sendTransaction_amountIsNegative() {
        assertTrue(send.amount < 0)
    }

    @Test
    fun transaction_amountValue() {
        // Send
        assertEquals(-612687, send.amount)
        assertEquals("0.00612687 BTC", send.cryptoValue())

        // Received
        assertEquals(559182, receive.amount)
        assertEquals("0.00559182 BTC", receive.cryptoValue())
    }

    @Test
    fun sendTransaction_feeValue() {
        // Send
        assertEquals(36881, send.fee)
        assertEquals("0.00036881 BTC", send.feeValue())

        // Received
        assertEquals(17996, receive.fee)
        assertEquals("0.00017996 BTC", receive.feeValue())
    }

    @Test
    fun sendTransaction_date() {
        assertEquals("Jan 11, 2019", send.date(false))
        assertEquals("Jan 11, 2019 18:13:10", send.date(true))
    }

    @Test
    fun sendTransaction_isSent() {
        assertTrue(send.isSent())
    }

    @Test
    fun sendTransaction_isNotReceive() {
        assertFalse(send.isReceive())
    }

    @Test
    fun transaction_hash() {
        assertEquals("cbc06203f949804a512290ade05dcab35cf30c16b43bb0ede6f5074f1f8c3b9e", send.hash)
    }

    @Test
    fun pendingTransaction_isPending() {
        assertTrue(send.isPending())
    }

    @Test
    fun receiveTransaction_amountIsPositive() {
        assertTrue(receive.amount > 0)
    }

    @Test
    fun receiveTransaction_isNotSent() {
        assertFalse(receive.isSent())
    }

    @Test
    fun receiveTransaction_isReceive() {
        assertTrue(receive.isReceive())
    }
}