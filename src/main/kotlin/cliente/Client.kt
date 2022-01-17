package cliente

import database.ListUsers
import java.io.DataInputStream
import java.io.DataOutputStream
import java.lang.Double.parseDouble
import java.net.Socket
import kotlin.system.exitProcess

class Client {
    private lateinit var socket: Socket
    private val port: Int = 1707
    private val host: String = "localhost"
    private lateinit var email: String
    private lateinit var password: String
    private var exit = false

    init {
        println("Please enter your email:")
        email = readLine().toString()
        println("Password:")
        password = readLine().toString()

        println("Connecting to $host in port $port as $email")
        socket = Socket(host, port)
        DataOutputStream(socket.getOutputStream()).writeUTF(email)
        DataOutputStream(socket.getOutputStream()).writeUTF(password)

        println(DataInputStream(socket.getInputStream()).readUTF())
        val access = DataInputStream(socket.getInputStream()).readInt()
        println(access)

        if (access == 1) {
            showMenu()
        } else {
            println("See you next time.")
        }
        exitProcess(0)
    }

    private fun showMenu() {
        while(!exit) {
            println("*** Welcome, please select a transaction: ***")
            println()
            println("1. Withdraw cash.")
            println("2. Consult balance.")
            println("3. Make deposit.")
            println("4. Exit.")
            var option = ""
            while (!option.matches("[1-4]".toRegex())) {
                option = readLine().toString()
            }
            submenu(option)
        }
    }

    private fun submenu(option: String) {
        when (option) {
            "1" -> {
                withdraw()
            }
            "2" -> {
                balance()
            }
            "3" -> {
                deposit()
            }
            "4" -> {
                exit()
            }
        }
    }

    private fun withdraw() {
        DataOutputStream(socket.getOutputStream()).writeInt(1)
        println("How much cash do you wish to withdraw?")
        var cash: Double = 0.0
        while(cash <= 0.0) {
            cash = parseDouble(readLine())
        }
        try {
            DataOutputStream(socket.getOutputStream()).writeDouble(cash)
            println(DataInputStream(socket.getInputStream()).readUTF())
        } catch (ex: Exception) {
            println("Amount not accepted.")
        }
    }

    private fun balance() {
        DataOutputStream(socket.getOutputStream()).writeInt(2)
        println(DataInputStream(socket.getInputStream()).readUTF())
    }

    private fun deposit() {
        DataOutputStream(socket.getOutputStream()).writeInt(3)
        println("How much money do you wish to deposit?")
        var cash: Double = 0.0
        while(cash <= 0.0) {
            cash = parseDouble(readLine())
        }
        try {
            DataOutputStream(socket.getOutputStream()).writeDouble(cash)
            println(DataInputStream(socket.getInputStream()).readUTF())
        } catch (ex: Exception) {
            println("Amount not accepted.")
        }
    }

    private fun exit() {
        DataOutputStream(socket.getOutputStream()).writeInt(4)
        exit = true
        println("See you next time.")
    }
}


fun main() {
    Client()
}