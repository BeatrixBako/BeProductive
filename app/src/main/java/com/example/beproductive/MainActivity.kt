package com.example.beproductive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beproductive.ui.theme.BeProductiveTheme

//import androidx.lifecycle.viewmodel.compose.viewModel


data class Task(
    val name: String,
    var isSelected: Boolean = false
)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BeProductiveTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppContent()
                }
            }
        }
    }
}


@Composable
fun AppContent() {
    val tasks = remember {
        mutableStateListOf(
            Task("Task1"),
            Task("Task2"),
            Task("Task3"),
            Task("Task4"),
            Task("Task5"),
            Task("Task6"),
            Task("Task7"),
            Task("Task8"),
            Task("Task9"),
            Task("Task10")
        )
    }

    val checkedTasks = tasks.count { it.isSelected }
    //val progress = if (tasks.isNotEmpty()) checkedTasks / tasks.size.toFloat() else 0f

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

            //Footer with 'New Task' box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp) // Footer height
                    //.align(Alignment.BottomCenter) // Fix the footer at the bottom
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
                                tasks.add(Task(newTaskName.text))
                                newTaskName = TextFieldValue("")
                            }
                        },
                        onDeleteCompleted = {
                            tasks.removeAll { it.isSelected }
                        }
                    )
                }
            }

            //Progress Bar
            ProgressBar(checkedNumber = checkedTasks, totalNumber = tasks.size)

            //Scrollable List of Tasks
            ScrollableList(tasks = tasks)
        }
    }
}


@Composable
fun ProgressBar(checkedNumber: Int, totalNumber: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "Progress: $checkedNumber / $totalNumber",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(5.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = if (totalNumber > 0) checkedNumber / totalNumber.toFloat() else 0f,
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(16.dp)),
            color = Color(0xFF7D5260)
        )
    }
}


@Composable
fun ScrollableList(tasks: MutableList<Task>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
    ) {
        itemsIndexed(tasks) { index, task ->
            ListItem(
                task = task,
                onCheckedChange = { isChecked ->
                    tasks[index] = task.copy(isSelected = isChecked)
                }
            )
        }
    }
}


@Composable
fun ListItem(task: Task, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(
                if (task.isSelected) Color(0xFFCCC2DC) else Color(0xFFD0BCFF),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = task.isSelected,
            onCheckedChange = onCheckedChange
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = task.name,
            fontSize = 18.sp
        )
    }
}


@Composable
fun FooterAddTask(
    newTaskName: TextFieldValue,
    onNewTaskNameChange: (TextFieldValue) -> Unit,
    onAddTask: () -> Unit,
    onDeleteCompleted: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            TextField(
                value = newTaskName,
                onValueChange = onNewTaskNameChange,
                label = { Text("New Task") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))

            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {

            //Add new task button
            Button(
                onClick = onAddTask,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .width(180.dp)
            ) {
                Text(text = "Add")
            }

            Spacer(modifier = Modifier.width(8.dp))

            //Delete completed button
            Button(
                onClick = onDeleteCompleted,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .width(200.dp)
            ) {
                Text(text = "Delete Completed")
            }
        }

    }
}