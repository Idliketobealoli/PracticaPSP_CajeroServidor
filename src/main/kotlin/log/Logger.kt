package log

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

object Logger {
    private val path = "${System.getProperty("user.dir")}${File.separator}src" +
            "${File.separator}main${File.separator}resources${File.separator}logger.log"

    fun printLogMessage(code: Log, logMessage: String) {
        val writer = BufferedWriter(FileWriter(path, true))
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        val currentDate = simpleDateFormat.format(Date())
        writer.append("\n[$code] : $logMessage [${currentDate}]")
        writer.flush()
        writer.close()
    }
}