package io.aemare.include

import com.skype.*
import io.aemare.Constants
import io.aemare.agents.Agents

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
                        when {
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
                            Agents.checkMessage(content) -> {
                                try {
                                    val agent: String = Agents.agents.first()
                                    val fagent: Friend = Skype.getContactList().getFriend(agent)
                                    if (user.fullName == fagent.fullName) {
                                        user.send("If you want to help yourself, buy a rope.")
                                        return
                                    }
                                    user.chat().addUser(fagent)
                                    received.chat.setTopic("${Constants.NAME} Support #123456")
                                    received.chat.send("Hey ${user.fullName}, I have added you in a group with ${fagent.fullName}, he will help you further.")
                                    received.chat.leave()
                                    Agents.remove(agent)
                                    return
                                } catch (e: Exception) {
                                    println(e.printStackTrace())
                                    user.send("There is no agent available at this time, please try again later.")
                                }
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