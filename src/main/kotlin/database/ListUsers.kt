package database

import model.User
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.lang.Double.parseDouble
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import kotlin.collections.ArrayList

object ListUsers {
    var users: ArrayList<User> = loadData()

    private fun loadData(): ArrayList<User> {
        val path: Path = Paths.get("${System.getProperty("user.dir")}${File.separator}src" +
                "${File.separator}main${File.separator}resources${File.separator}users.csv")
        val lines: List<String>? = Files.readAllLines(path)
        val userList: ArrayList<User> = ArrayList<User>()
        lines?.forEach {
            val st = StringTokenizer(it,",")
            val user = User()
            user.name = st.nextToken()
            user.email = st.nextToken()
            user.password = st.nextToken()
            user.saldo = parseDouble(st.nextToken())
            user.limitSaldo = parseDouble(st.nextToken())
            userList.add(user)
        }
        return userList
    }

    fun addUser(user: User) {
        val path: Path = Paths.get("${System.getProperty("user.dir")}${File.separator}src" +
                "${File.separator}main${File.separator}resources${File.separator}users.csv")
        val file = File(path.toUri())
        val added = users.add(user)
        if (added) {
            val writer = BufferedWriter(FileWriter(file, true))
            writer.append("\n${user.name},${user.email},${user.password},${user.saldo},${user.limitSaldo}")
            writer.flush()
            writer.close()
        }
    }

    fun modifyBalance(email: String, newBalance: Double) {
        val path: Path = Paths.get("${System.getProperty("user.dir")}${File.separator}src" +
                "${File.separator}main${File.separator}resources${File.separator}users.csv")
        val file = File(path.toUri())
        val lines: ArrayList<String>? = Files.readAllLines(path) as ArrayList<String>?
        var lineToModify = ""

        lines?.forEach {
            val st = StringTokenizer(it, ",")
            st.nextToken()
            val emailFromCSV = st.nextToken()
            if (email == emailFromCSV) {
                lineToModify = it
            }
        }

        if(lineToModify != "") {
            lines?.remove(lineToModify)
        }


        val stokenizer = StringTokenizer(lineToModify,",")
        val modifiedUser = User()
        modifiedUser.name = stokenizer.nextToken()
        modifiedUser.email = stokenizer.nextToken()
        modifiedUser.password = stokenizer.nextToken()
        stokenizer.nextToken()
        modifiedUser.saldo = newBalance
        modifiedUser.limitSaldo = parseDouble(stokenizer.nextToken())
        val added = users.add(modifiedUser)
        if (added) {
            val writer = BufferedWriter(FileWriter(file, true))
            writer.append("\n${modifiedUser.name},${modifiedUser.email},${modifiedUser.password},${modifiedUser.saldo},${modifiedUser.limitSaldo}")
            writer.flush()
            writer.close()
        }
    }
}