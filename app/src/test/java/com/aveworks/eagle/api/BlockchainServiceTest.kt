package com.aveworks.eagle.api

import com.aveworks.common.data.MultiAddressResponse
import com.aveworks.eagle.TestUtils
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.observers.TestObserver
import junit.framework.TestCase
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import retrofit2.Response
import retrofit2.Retrofit

class BlockchainServiceTest {
    private lateinit var response: TestObserver<MultiAddressResponse>

    @get:Rule
    val mockWebServer = MockWebServer()

    private val blockchainService by lazy {
        BlockchainService.create(mockWebServer.url("/"))
    }

    @Before
    fun multiAddress_response() {

        mockWebServer.enqueue(
            MockResponse()
                .setBody(TestUtils.readFromFile("multiaddr.json"))
                .setResponseCode(200))

        response = blockchainService.multiAddressObservable("dummy").test()
    }

    @Test
    fun testMultiAddress_endpoint() {
        response.assertNoErrors()
        assertEquals("/multiaddr?active=dummy&n=50&offset=0", mockWebServer.takeRequest().path)
    }
}