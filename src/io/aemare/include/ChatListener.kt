package io.aemare.include

import com.skype.*
import io.aemare.Constants

/**
 * Property of Aemare
 */
object ChatListener {

    fun run() {
        var lock: Boolean = Constants.LOCK
        val password: Array<String> = Constants.PASSWORDS

        Skype.addChatMessageListener(object: ChatMessageAdapter() {
            @Throws(SkypeException::class)
            override fun chatMessageReceived(received: ChatMessage) {
                if (received.type.equals(ChatMessage.Type.SAID)) {
                    val content: String = received.content.toLowerCase()
                    val user: User = received.sender
                    try {
                        when { //TODO make the script read input and responses from a list, create variation in what Spectrum says.
                            content.startsWith("lock") && content.endsWith(password[0]) -> {
                                if (lock) lock = false else lock = true
                                user.send("${Constants.NAME} has been ${if (lock) "locked" else "unlocked"} for termination.")
                                return
                            }
                            content.startsWith("kill") && content.endsWith(password[1]) && !lock -> {
                                user.send("${Constants.NAME} has been terminated.")
                                System.exit(1)
                                return
                            }
                            content.contains("i need help") -> {
                                var agent: Friend = Skype.getContactList().getFriend("echo123") //TODO get an available agent from a pool.
                                user.chat().addUser(agent)
                                received.chat.setTopic("${Constants.NAME} Support #123456") //TODO generate a legit ID and store this together with the agent and the user their names.
                                received.chat.send("Hey ${user.fullName}, I have added you in a group with ${agent.fullName}, he will help you further.")
                                received.chat.leave()

                                return
                            }
                            else -> {
                                user.send("Welcome to ${Constants.NAME}, ${user.fullName}.")
                                return
                            }
                        }
                    } catch (e:Exception) {
                        user.send("${Constants.NAME} found an error, try again.")
                    }
                }
            }
        })
    }
}