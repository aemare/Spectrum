package io.aemare

import com.skype.Chat
import com.skype.Friend
import com.skype.Group
import com.skype.Skype
import io.aemare.agents.Agents
import io.aemare.include.RequestListener
import io.aemare.include.CallListener
import io.aemare.include.ChatListener

object Executor {

    @JvmStatic fun main(args:Array<String>) {
        println("${Constants.NAME} is initializing.")
        try {
            Skype.setDaemon(Constants.DAEMON)
            Skype.setDebug(Constants.DEBUG)
            if (!Constants.DEBUG) {
                Skype.getProfile().fullName = Constants.NAME
                Skype.getProfile().moodMessage = Constants.MOOD
                Skype.getProfile().introduction = Constants.DESCRIPTION
                Skype.getProfile().webSiteAddress = Constants.WEBSITE
            }
            ChatListener.run()
            CallListener.run()
            RequestListener.run()
            Agents.run()
            println("${Constants.NAME} has been initialized.")
        } catch (e: Exception) {
            e.printStackTrace()
            println("${Constants.NAME} couldn't be initialized.")
        }

    }
}

