package com.narehase.air_gui

import java.util.Timer
import java.util.TimerTask

class Utilcls {
    object UtilCls {
        @JvmStatic
        fun DelayTask(runnable: Runnable, delay: Int, runonce: Boolean): TimerTask {
            val timer = Timer()
            val task: TimerTask = object : TimerTask() {
                override fun run() {
                    runnable.run()
                    if (runonce) {
                        cancel()
                    }
                }
            }
            timer.schedule(task, delay.toLong())
            return task
        }
    }
}