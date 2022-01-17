package servidor

import java.net.ServerSocket
import java.net.Socket

class Server {
    private val port: Int = 1707
    private val maxConnections: Int = 5
    lateinit var server: ServerSocket
    lateinit var socket: Socket

    init {
        try {
            server = ServerSocket(port, maxConnections)

            while (true) {
                println("Server waiting...")
                socket = server.accept()
                ClientManager(socket).start()
                println("Client with IP ${socket.inetAddress.hostName} connected successfully.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun main() {
    Server()
}