package com.godwin.communication

import java.net.Socket

interface MessageContract {
    fun sendMessage(message: String)
    fun getSocket(): Socket
    fun close()
}