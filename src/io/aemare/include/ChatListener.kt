package io.aemare.include

import com.skype.*
import io.aemare.Constants

object ChatListener {

    fun run() {
        var lock: Boolean = Constants.LOCK
        val password: Array<String> = Constants.PASSWORDS

        Skype.addChatMessageListener(object: ChatMessageAdapter() {
            @Throws(SkypeException::class)
            override fun chatMessageReceived(received: ChatMessage) {
                if (received.type.equals(ChatMessage.Type.SAID)) {
                    val content: String = received.content
                    val user: User = received.sender
                    try {
                        when {
                            content.startsWith("lock") && content.endsWith(password[0]) -> {
                                if (lock) lock = false else lock = true
                                user.send("Spectrum has been ${if (lock) "locked" else "unlocked"} for termination.")
                            }
                            content.startsWith("kill") && content.endsWith(password[1]) && !lock -> {
                                user.send("Spectrum has been terminated.")
                                System.exit(1)
                            }
                            else -> {
                                user.send("Welcome to Spectrum, ${user.fullName}.")
                                return
                            }
                        }
                    } catch (e:Exception) {
                        user.send("I found an error, try again.")
                    }
                }
            }
        })
    }
}