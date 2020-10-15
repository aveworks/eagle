package com.aveworks.eagle

import com.aveworks.eagle.api.BlockchainService
import com.aveworks.eagle.viewmodels.TransactionListViewModel
import io.reactivex.rxjava3.kotlin.subscribeBy
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.InputStreamReader
import org.junit.Assert.*

class BlockchainServiceTest {

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(200)
                    .setBody(readFromFile("multiaddr.json"))
            }
        }
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testMultiAddress() {

        var httpResponse =
            BlockchainService.create(mockWebServer.url("/")).multiAddress("dummy").execute()

        assertTrue(httpResponse.isSuccessful)

        var response = httpResponse.body()!!

        val txs = response.txs
        val wallet = response.wallet
        val info = response.info


        assertEquals(txs.size, 50)
        assertEquals(wallet.finalBalance, 8549)
        assertEquals(info.local["conversion"] as? Double, 8769.98350366)


        val txSend = txs[0]
        val txReceive = txs[1]

        // Test Send Transaction
        assertTrue(txSend.isSent())
        assertFalse(txSend.isReceive())
        assertEquals(
            "cbc06203f949804a512290ade05dcab35cf30c16b43bb0ede6f5074f1f8c3b9e",
            txSend.hash
        )
        assertEquals(-612687, txSend.amount)
        assertTrue(txSend.amount < 0)
        assertEquals("0.00612687 BTC", txSend.cryptoValue())

        // Test Receive Transaction
        assertFalse(txReceive.isSent())
        assertTrue(txReceive.isReceive())
        assertEquals(
            "4524ce25c3134b42970dd94c6d2096a81dc9fb7381b986fe5eb57d98ede7655d",
            txReceive.hash
        )
        assertEquals(559182, txReceive.amount)
        assertTrue(txReceive.amount > 0)
        assertEquals("0.00559182 BTC", txReceive.cryptoValue())

        // fiatAmount is only available from the Observable call
        assertNull(txSend.fiatAmount)
    }

    fun readFromFile(path: String): String {
        val reader = InputStreamReader(this.javaClass.classLoader?.getResourceAsStream(path))
        val contents = reader.readText()
        reader.close()
        return contents
    }

}