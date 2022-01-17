package servidor

import database.ListUsers
import log.Log
import log.Logger
import model.User
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

class ClientManager(socket: Socket) : Thread() {
    val client = socket
    lateinit var mail : String
    var exit = false

    override fun run() {
        if(checkIfUserExists()) {
            DataOutputStream(client.getOutputStream()).writeUTF("User accepted, login successful.")
            DataOutputStream(client.getOutputStream()).writeInt(1)
            writeInLog(3,"")
            while (!exit) {
                when (DataInputStream(client.getInputStream()).readInt()) {
                    1 -> withdraw()
                    2 -> balance()
                    3 -> deposit()
                    4 -> logOut()
                }
            }
        } else {
            writeInLog(5,"Incorrect email or password.")
            DataOutputStream(client.getOutputStream()).writeUTF("Incorrect email or password.")
            DataOutputStream(client.getOutputStream()).writeInt(0)
            client.close()
        }
    }

    private fun logOut() {
        exit = true
        println("$mail logged out.")
        writeInLog(4, "")
        client.close()
    }

    private fun deposit() {
        val cash = DataInputStream(client.getInputStream()).readDouble()
        ListUsers.users.first { x -> x.email == mail }.saldo += cash
        writeInLog(2, "Added $cash €")
        DataOutputStream(client.getOutputStream()).writeUTF("Added $cash € to your balance.")
    }

    private fun balance() {
        val balance = ListUsers.users.first { x -> x.email == mail }.saldo
        DataOutputStream(client.getOutputStream()).writeUTF("Your current balance is $balance")
        writeInLog(1, "Current balance -> $balance")
    }

    private fun withdraw() {
        val cash = DataInputStream(client.getInputStream()).readDouble()
        val userSaldo = ListUsers.users.first { x -> x.email == mail }.saldo
        val userLimit = ListUsers.users.first { x -> x.email == mail }.limitSaldo
        if (cash <= userSaldo && cash <= userLimit) {
            ListUsers.users.first { x -> x.email == mail }.saldo -= cash
            writeInLog(0, "Withdrawed $cash €")
            DataOutputStream(client.getOutputStream()).writeUTF("Withdrawed $cash € from your balance.")
        } else {
            writeInLog(5, "Insufficient balance.")
            DataOutputStream(client.getOutputStream()).writeUTF("Insufficient balance.")
        }
    }

    private fun writeInLog(i: Int, s: String) {
        when(i) {
            0 -> Logger.printLogMessage(Log.WITHDRAW, "Withdraw operation in account $mail : $s")
            1 -> Logger.printLogMessage(Log.BALANCE, "Balance checked in account $mail : $s")
            2 -> Logger.printLogMessage(Log.DEPOSIT, "Deposit operation in account $mail : $s")
            3 -> Logger.printLogMessage(Log.LOG_IN, "Logged into account: $mail")
            4 -> Logger.printLogMessage(Log.LOG_OUT, "Logged out of account: $mail")
            5 -> Logger.printLogMessage(Log.ERROR, "Error detected : $s")
        }
    }

    private fun checkIfUserExists(): Boolean {
        var users = ListUsers.users
        val email : String = DataInputStream(client.getInputStream()).readUTF()
        val password : String = DataInputStream(client.getInputStream()).readUTF()
        users = users.filter { x -> x.email == email && x.password == password
        } as ArrayList<User>
        if (users.isNotEmpty()) {
            mail = users.first().email
        }
        return users.isNotEmpty()
    }
}