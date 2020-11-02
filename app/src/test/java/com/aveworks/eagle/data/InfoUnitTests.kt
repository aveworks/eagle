package com.aveworks.eagle.data

import com.aveworks.common.data.Info
import com.aveworks.eagle.TestUtils
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Assert.*
import org.junit.Test
import org.junit.BeforeClass


// TODO move tests to common
class InfoUnitTests {

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