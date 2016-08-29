package io.aemare.agents

import com.skype.SkypeException
import io.aemare.Constants
import java.io.BufferedReader
import java.io.FileReader
import java.util.*
import java.sql.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object Agents {

    var agents: ArrayList<String> = ArrayList()

    val executor = Executors.newScheduledThreadPool(1)

    val messages: ArrayList<String> = ArrayList()

    fun run() {
        executor.scheduleAtFixedRate({
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val query: String = "SELECT `username` FROM `agents` WHERE `status` = 1"
                val url = "jdbc:mysql://${Constants.SQL_SERVER}:${Constants.SQL_PORT}/${Constants.SQL_DB}"
                val conn = DriverManager.getConnection(url, Constants.SQL_USER, Constants.SQL_PASSWORD)
                val stmt = conn.createStatement()
                val rs: ResultSet

                rs = stmt.executeQuery(query)
                while (rs.next()) {
                    val username = rs.getString("username")
                    if (!agents.contains(username)) {
                        agents.add(username)
                        println(username)
                    }
                }
                conn.close()

            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }, 0, 1, TimeUnit.SECONDS)
        val file = BufferedReader(FileReader("data/lines.txt"))
        file.lines().forEach { i -> messages.add(i)}
        file.close()
    }

    fun remove(agent: String) {
        try {
            try {
                agents.remove(agent)
                Class.forName("com.mysql.jdbc.Driver")
                var query = "UPDATE `agents` SET `status` = 0 WHERE `username` = '$agent'"
                val url = "jdbc:mysql://${Constants.SQL_SERVER}:${Constants.SQL_PORT}/${Constants.SQL_DB}"
                val conn = DriverManager.getConnection(url, Constants.SQL_USER, Constants.SQL_PASSWORD)
                val stmt = conn.createStatement()
                stmt.executeUpdate(query)
                conn.close()
            } catch (se: SQLException) {
                se.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } catch (ex: SkypeException) {
            ex.printStackTrace()
        }
    }

    fun checkMessage(msg: String): Boolean {
        return messages.contains(msg)
    }
}