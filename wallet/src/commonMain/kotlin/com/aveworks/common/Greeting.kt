package com.aveworks.common

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}