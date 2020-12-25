package com.fighter.lancomm_demo

import com.fighter.lancomm.ptop.Command
import com.fighter.lancomm_kt.sendCommand

/**
 * @author fighter_lee
 * @date 2020/12/25
 * @description
 */
object KtExecuteDemo {

    fun sendCommandTest() {
        sendCommand{
            destIp = "192.168.1.13"
            data = "我点你了，哈哈~".toByteArray()
            callback = object : Command.Callback {
                override fun onSuccess() {
                }

                override fun onReceived() {
                }

                override fun onError(code: Int) {
                }
            }
        }
    }

}