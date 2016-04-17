package io.aemare.include

import com.skype.*

object CallListener {

    fun run() {
        Skype.addCallListener(object: CallAdapter() {
            @Throws(SkypeException::class)
            override fun callMaked(makedCall: Call?) { makedCall?.finish() }

            @Throws(SkypeException::class)
            override fun callReceived(receivedCall: Call?) { receivedCall?.finish(); }
        })
    }
}
