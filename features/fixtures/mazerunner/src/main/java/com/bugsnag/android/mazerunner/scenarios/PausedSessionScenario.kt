package com.bugsnag.android.mazerunner.scenarios

import android.content.Context
import com.bugsnag.android.Bugsnag
import com.bugsnag.android.Configuration
import com.bugsnag.android.flushAllSessions
import android.os.Handler
import android.os.HandlerThread
import android.util.Log

/**
 * Sends an exception after pausing the session
 */
internal class PausedSessionScenario(config: Configuration,
                                     context: Context) : Scenario(config, context) {

    companion object {
        private const val SLEEP_MS: Long = 100
    }

    init {
        config.autoTrackSessions = false
    }

    override fun run() {
        super.run()
        val client = Bugsnag.getClient()
        val thread = HandlerThread("HandlerThread")
        thread.start()

        Handler(thread.looper).post(Runnable {
            // send 1st exception which should include session info
            client.startSession()
            Log.d("Bugsnag - Stopped", "First session started")
            Thread.sleep(SLEEP_MS)
            flushAllSessions()
            Log.d("Bugsnag - Stopped", "First session flushed")
            Thread.sleep(SLEEP_MS)
            client.notify(generateException())
            Log.d("Bugsnag - Stopped", "First exception sent")
            Thread.sleep(SLEEP_MS)

            // send 2nd exception which should not include session info
            client.pauseSession()
            Log.d("Bugsnag - Stopped", "First session paused")
            Thread.sleep(SLEEP_MS)
            flushAllSessions()
            Log.d("Bugsnag - Stopped", "First session flushed (again)")
            Thread.sleep(SLEEP_MS)
            client.notify(generateException())
            Log.d("Bugsnag - Stopped", "Second exception sent")
        })
    }
}
