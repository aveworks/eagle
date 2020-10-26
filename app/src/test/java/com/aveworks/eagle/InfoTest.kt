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
class InfoTest {

    companion object{
        private lateinit var info: Info

        @BeforeClass
        @JvmStatic
        fun setup() {
            val response = TestUtils.getMultiAddressResponse()

            info = response.info
        }
    }

    @Test
    fun testInfo_conversion() {
        assertEquals(8769.98350366, info.local["conversion"]?.jsonPrimitive?.double ?: 0)
    }
}