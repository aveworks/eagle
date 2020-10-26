package com.aveworks.eagle

import com.aveworks.common.data.MultiAddressResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.InputStreamReader

object TestUtils{
    fun readFromFile(path: String): String {
        val reader = InputStreamReader(javaClass.classLoader?.getResourceAsStream(path))
        val contents = reader.readText()
        reader.close()
        return contents
    }

    fun getMultiAddressResponse(): MultiAddressResponse{
        return Json {
            ignoreUnknownKeys = true
            isLenient = true
        }.decodeFromString<MultiAddressResponse>(TestUtils.readFromFile("multiaddr.json"))
    }
}