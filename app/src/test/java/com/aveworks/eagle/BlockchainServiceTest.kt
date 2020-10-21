package com.aveworks.eagle

import com.aveworks.eagle.api.BlockchainService
import com.aveworks.eagle.viewmodels.TransactionListViewModel
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.serialization.json.double
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonPrimitive
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
                    .setBody(TestUtils.readFromFile("multiaddr.json"))
            }
        }
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testMultiAddress_response() {

        var httpResponse =
            BlockchainService.create(mockWebServer.url("/")).multiAddress("dummy").execute()

        assertTrue(httpResponse.isSuccessful)

        var response = httpResponse.body()!!

        val txs = response.txs
        val wallet = response.wallet
        val info = response.info


        assertEquals(txs.size, 50)
        assertEquals(wallet.finalBalance, 8549)
        assertEquals(info.local["conversion"]?.jsonPrimitive?.double, 8769.98350366)
    }
}