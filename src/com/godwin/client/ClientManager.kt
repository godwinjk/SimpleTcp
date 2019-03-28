package com.godwin.client

import com.godwin.communication.MessageContract
import com.godwin.serialinterface.connection.TcpCallbackSubscriber
import com.godwin.serialinterface.worker.ThreadPoolProvider
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

class ClientManager : MessageContract {

    private var mSocket: Socket? = null
    private var inStream: DataInputStream? = null
    private var outStream: DataOutputStream? = null
    private var isClosed = false

    private val runnable = Runnable {
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

    fun connect(ip: String, port: Int) {
        mSocket = Socket(ip, port)

        try {
            inStream = DataInputStream(mSocket!!.getInputStream())
            outStream = DataOutputStream(mSocket!!.getOutputStream())

            ThreadPoolProvider.executeBackGroundTask(runnable)

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
        mSocket!!.close()
        isClosed = true
        TcpCallbackSubscriber.publishOnClosed(this)
    }

    override fun getSocket(): Socket {
        return mSocket!!
    }

}