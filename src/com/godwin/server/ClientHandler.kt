package com.godwin.server

import com.godwin.communication.MessageContract
import com.godwin.serialinterface.connection.TcpCallbackSubscriber
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

class ClientHandler(socket: Socket) : Runnable, MessageContract {
    private val mSocket: Socket = socket
    private var inStream: DataInputStream? = null
    private var outStream: DataOutputStream? = null
    private var isClosed = false

    init {
        try {
            inStream = DataInputStream(mSocket.getInputStream())
            outStream = DataOutputStream(mSocket.getOutputStream())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun run() {
        try {
            var clientMessage: String
            while (!isClosed) {
                clientMessage = inStream!!.readUTF()
                println(clientMessage)

                TcpCallbackSubscriber.publishOnMessage(clientMessage, this)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun sendMessage(message: String) {
        if (outStream != null) {
            outStream!!.writeUTF(message)
            outStream!!.flush()
        }
    }

    override fun close() {
        if (inStream != null) {
            inStream!!.close()
        }
        if (outStream != null) {
            outStream!!.close()
        }
        mSocket.close()
        isClosed = true
        TcpCallbackSubscriber.publishOnClosed(this)
    }

    override fun getSocket(): Socket {
        return mSocket
    }
}