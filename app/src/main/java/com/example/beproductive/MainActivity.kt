package com.example.beproductive

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.beproductive.FirebaseTaskHelper.updateTaskStatus
import com.example.beproductive.ui.theme.BeProductiveTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


data class Task(
    var id: String = "",
    var name: String = "",
    var isSelected: Boolean = false
)


class MainActivity : ComponentActivity() {
    //private val tasks = mutableStateListOf<Task>()
    private val database = Firebase.database("https://beproductive-f7dd2-default-rtdb.europe-west1.firebasedatabase.app/")
    private val tasksRef = database.getReference("tasks")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Loads all the tasks from the XML file
        //tasks.addAll(TaskStorage.loadTasks(this))
        enableEdgeToEdge()
        setContent {
            BeProductiveTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var tasks by remember { mutableStateOf(listOf<Task>()) }

                    // Add a Firebase listener to load tasks
                    tasksRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val taskList = mutableListOf<Task>()
                            for (taskSnapshot in snapshot.children) {
                                val task = taskSnapshot.getValue(Task::class.java)
                                if (task != null) {
                                    task.id = taskSnapshot.key ?: ""
                                    taskList.add(task)
                                }
                            }
                            tasks = taskList
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e("Firebase", "Error loading tasks", error.toException())
                        }
                    })


                    AppContent(tasks = tasks, onTaskStatusChange = { taskId, isSelected ->
                        updateTaskStatus(taskId, isSelected) }
                        /*tasks = tasks, onSaveTasks = { saveTasks() }*/
                    )
                }
            }
        }
    }

    //Functions for XML file usage
    /*
    override fun onPause() {
        super.onPause()
        // Save tasks when the app goes into the background
        saveTasks()
    }

    private fun saveTasks() {
        TaskStorage.saveTasks(this, tasks)
    }
     */
}