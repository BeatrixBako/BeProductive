package com.example.beproductive

import android.content.Context
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStreamWriter

//Functions to handle XML
object TaskStorage {
    private const val FILE_NAME = "tasks.xml"

    // Save tasks to an XML file
    fun saveTasks(context: Context, tasks: List<Task>) {
        val file = File(context.filesDir, FILE_NAME)
        val outputStream = FileOutputStream(file)
        val writer = OutputStreamWriter(outputStream)

        writer.write("<tasks>\n")
        for (task in tasks) {
            writer.write("    <task>\n")
            writer.write("        <id>${task.id}</id>\n")
            writer.write("        <name>${task.name}</name>\n")
            writer.write("        <isSelected>${task.isSelected}</isSelected>\n")
            writer.write("    </task>\n")
        }
        writer.write("</tasks>\n")
        writer.close()
    }

    // Load tasks from an XML file
    fun loadTasks(context: Context): List<Task> {
        val file = File(context.filesDir, FILE_NAME)
        if (!file.exists()) {
            return emptyList()
        }

        val tasks = mutableListOf<Task>()
        val inputStream = FileInputStream(file)

        val factory = XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()
        parser.setInput(inputStream, null)

        var eventType = parser.eventType
        var id: String? = null
        var name: String? = null
        var isSelected: Boolean? = null

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    when (parser.name) {
                        "id" -> id = parser.nextText()
                        "name" -> name = parser.nextText()
                        "isSelected" -> isSelected = parser.nextText().toBoolean()
                    }
                }
                XmlPullParser.END_TAG -> {
                    if (parser.name == "task" && id != null && name != null && isSelected != null) {
                        tasks.add(Task(id, name, isSelected))
                        id = null
                        name = null
                        isSelected = null
                    }
                }
            }
            eventType = parser.next()
        }
        inputStream.close()
        return tasks
    }
}
