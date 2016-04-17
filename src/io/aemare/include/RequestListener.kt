package io.aemare.include

import com.skype.Skype
import com.skype.SkypeException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object RequestListener {

    val executor = Executors.newScheduledThreadPool(1)

    fun run() {
        executor.scheduleAtFixedRate({
            try {
                accept()
            } catch (ex: SkypeException) {
                ex.printStackTrace()
            }
        }, 0, 15, TimeUnit.SECONDS)
    }

    @Throws(SkypeException::class)
    fun accept():Int {
        val waitingContracts = Skype.getContactList().allUserWaitingForAuthorization
        var counter: Int = 0
        for (user in waitingContracts)
        {
            user.isAuthorized = true
            counter++
        }
        return counter
    }
}