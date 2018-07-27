package com.alexstyl.specialdates.dailyreminder

import android.content.Context
import com.alexstyl.specialdates.dailyreminder.log.LoggedEventsRepository
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class AndroidLoggedEventsRepository(private val context: Context) : LoggedEventsRepository {

    override fun writeEvents(events: String) {
        try {
            val outputStreamWriter = OutputStreamWriter(context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE))
            outputStreamWriter.write(events)
            outputStreamWriter.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun fetchAllEvents(): String {
        val stringBuilder = StringBuilder()
        try {
            val inputStream = context.openFileInput(FILE_NAME)

            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            var receiveString: String? = ""

            receiveString = bufferedReader.readLine()
            while (receiveString != null) {
                stringBuilder.appendln(receiveString)
                receiveString = bufferedReader.readLine()
            }
            inputStream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return stringBuilder.toString()
    }

    companion object {
        const val FILE_NAME = "debug_daily_reminder.log"
    }

}