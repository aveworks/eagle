package com.aveworks.eagle

import java.io.InputStreamReader

object TestUtils{
    fun readFromFile(path: String): String {
        val reader = InputStreamReader(javaClass.classLoader?.getResourceAsStream(path))
        val contents = reader.readText()
        reader.close()
        return contents
    }
}