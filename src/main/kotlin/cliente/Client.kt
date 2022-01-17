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
        println("What do you want to do?" +
                "\n1. Log in." +
                "\n2. Create an account (exit afterwards).")
        var option = ""
        while (!option.matches("[1-2]".toRegex())) {
            option = readLine().toString()
        }
        if (option == "1") {
            println("Please enter your email:")
            email = readLine().toString()
            println("Password:")
            password = readLine().toString()

            println("Connecting to $host in port $port as $email")
            socket = Socket(host, port)
            DataOutputStream(socket.getOutputStream()).writeInt(1)
            DataOutputStream(socket.getOutputStream()).writeUTF(email)
            DataOutputStream(socket.getOutputStream()).writeUTF(password)

            println(DataInputStream(socket.getInputStream()).readUTF())
            val access = DataInputStream(socket.getInputStream()).readInt()
            println(access)

            if (access == 1) {
                showMenu()
            } else {
                println("See you next time.")
                exitProcess(0)
            }
        } else {
            var dataAreCorrect = false
            var newName: String = ""
            var newEmail: String = ""
            var newPassword: String = ""
            var repeatedPassword: String = ""
            while (!dataAreCorrect) {
                println("Please enter your name:")
                newName = readLine().toString()
                println("Please enter your email:")
                newEmail = readLine().toString()
                println("Password:")
                newPassword = readLine().toString()
                println("Repeat password:")
                repeatedPassword = readLine().toString()
                if (newPassword == repeatedPassword && newEmail.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})\$".toRegex())) {
                    dataAreCorrect = true
                }
            }
            socket = Socket(host, port)
            DataOutputStream(socket.getOutputStream()).writeInt(2)
            DataOutputStream(socket.getOutputStream()).writeUTF(newEmail)
            DataOutputStream(socket.getOutputStream()).writeUTF(newName)
            DataOutputStream(socket.getOutputStream()).writeUTF(newPassword)
            println("Creating account. Wait a moment $newName")
            println(DataInputStream(socket.getInputStream()).readUTF())
            exitProcess(0)
        }
    }

    private fun showMenu() {
        while(!exit) {
            println("*** Welcome, please select a transaction: ***" +
                    "\n\n1. Withdraw cash." +
                    "\n2. Consult balance." +
                    "\n3. Make deposit." +
                    "\n4. Exit.")
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
            cash = try {
                readLine()?.toDouble() ?: -1.0
            } catch (e: Exception) {
                -1.0
            }
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
            cash = try {
                readLine()?.toDouble() ?: -1.0
            } catch (e: Exception) {
                -1.0
            }
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
        exitProcess(0)
    }
}


fun main() {
    Client()
}