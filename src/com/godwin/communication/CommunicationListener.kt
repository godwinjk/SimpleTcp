package com.godwin.communication

import com.godwin.util.Error

interface CommunicationListener {

    fun onClientConnected(socket: MessageContract)

    fun onMessage(message: String, socket: MessageContract)

    fun onClientDisconnected(socket: MessageContract, error: Error?)
}