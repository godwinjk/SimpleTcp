package com.godwin.communication

import java.net.Socket

interface MessageCallback {

    fun sendMessage(message: String, socket: Socket)

}