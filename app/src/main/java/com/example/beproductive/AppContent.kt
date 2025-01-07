package com.example.beproductive

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beproductive.FirebaseTaskHelper.updateTaskStatus

@Composable
fun AppContent(tasks: List<Task>,
               onTaskStatusChange: (String, Boolean) -> Unit)
        /*(tasks: SnapshotStateList<Task>,
                       onSaveTasks: () -> Unit)*/
{

    val context = LocalContext.current

    val checkedTasks = tasks.count { it.isSelected }

    var newTaskName by remember { mutableStateOf(TextFieldValue("")) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
        ) {
            //Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFEFB8C8))
            ) {
                Text(
                    text = "Be Productive",
                    fontSize = 40.sp,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(25.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .height(100.dp)
                        .background(Color(0xFF7D5260))
                )
            }

            //Footer with 'New Task' box and buttons
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp) // Footer height
                    .clip(RoundedCornerShape(16.dp)) // Rounded top corners
                    .background(Color(0xFFEFB8C8))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFEFB8C8))
                        .clip(RoundedCornerShape(16.dp))
                        .padding(horizontal = 16.dp), // Add padding to the sides
                    horizontalArrangement = Arrangement.SpaceEvenly, // Evenly space buttons
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FooterAddTask(
                        newTaskName = newTaskName,
                        onNewTaskNameChange = { newTaskName = it },
                        onAddTask = {
                            if (newTaskName.text.isNotBlank()) {
                                val newTask = Task(name = newTaskName.text)

                                // Add to Firebase
                                FirebaseTaskHelper.addTask(newTask.name)

                                // Clear input
                                newTaskName = TextFieldValue("")
                            } else {
                                Toast.makeText(context, "Task name cannot be empty", Toast.LENGTH_SHORT).show()
                            }
                        },
                        onDeleteCompleted = {
                            //Deletes tasks form firebase by id, which is found based on the name of the task
                            tasks.filter { it.isSelected }.forEach{ task ->
                                FirebaseTaskHelper.deleteTask(task.id)
                            }
                        }

                        //Functions for XML file usage
                        /*
                        * onAddTask = {
                            if (newTaskName.text.isNotBlank()) {
                                val newTask = Task(name = newTaskName.text)

                                // Add to local list
                                tasks.add(Task(newTaskName.text))

                                // Clear input
                                newTaskName = TextFieldValue("")

                                //Add to XML file
                                TaskStorage.saveTasks(context, tasks) // Save tasks
                            } else {
                                Toast.makeText(context, "Task name cannot be empty", Toast.LENGTH_SHORT).show()
                            }
                        },
                        onDeleteCompleted = {
                            //Deletes task from local list
                            tasks.removeAll { it.isSelected }

                            onSaveTasks()

                            //Updates XML file
                            TaskStorage.saveTasks(context, tasks) // Save tasks

                        }*/
                    )
                }
            }

            //Progress Bar
            ProgressBar(checkedNumber = checkedTasks, totalNumber = tasks.size)

            //Scrollable List of Tasks
            ScrollableList(tasks = tasks, onTaskStatusChange = { taskId, isSelected ->
                updateTaskStatus(taskId, isSelected) }
                /*(tasks = tasks)*/)
        }
    }
}
