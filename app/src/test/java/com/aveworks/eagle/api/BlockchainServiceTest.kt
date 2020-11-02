package com.aveworks.eagle.api

import com.aveworks.common.data.MultiAddressResponse
import com.aveworks.eagle.TestUtils
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import retrofit2.Response

class BlockchainServiceTest {
    private var response : Response<MultiAddressResponse>? = null

    companion object{
        private lateinit var mockWebServer: MockWebServer

        @BeforeClass
        @JvmStatic
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

        @AfterClass
        @JvmStatic
        fun teardown() {
            mockWebServer.shutdown()
        }
    }

    @Before
    fun multiAddress_response() {
        response = BlockchainService.create(mockWebServer.url("/")).multiAddress("dummy").execute()
    }

    @Test
    fun testMultiAddress_isSuccessful() {
        assertTrue(response!!.isSuccessful)
    }

    @Test
    fun testMultiAddress_hasWallet() {
        assertEquals(8549L, response?.body()?.wallet?.finalBalance)
    }

    @Test
    fun testMultiAddress_hasInfo() {
        assertEquals(8769.98350366, response?.body()?.info?.local?.get("conversion")?.jsonPrimitive?.double ?: 0.0, 0.0)
    }

    @Test
    fun testMultiAddress_hasTxs() {
        assertEquals(50, response?.body()?.txs?.size)
    }
}