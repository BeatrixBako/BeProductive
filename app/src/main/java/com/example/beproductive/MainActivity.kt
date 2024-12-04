package com.example.beproductive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beproductive.ui.theme.BeProductiveTheme

//import androidx.lifecycle.viewmodel.compose.viewModel

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
            Pair("Task1", false),
            Pair("Task2", false),
            Pair("Task3", false),
            Pair("Task4", false),
            Pair("Task5", false),
            Pair("Task6", false),
            Pair("Task7", false),
            Pair("Task8", false),
            Pair("Task9", false),
            Pair("Task10", false),
        )
    }

    val checkedTasks = tasks.count { it.second }
    val progress = if (tasks.isNotEmpty()) checkedTasks / tasks.size.toFloat() else 0f



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // Main Content: Header, Progress Bar and Scrollable List
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
            //.background(Color(0xFF6650a4))
        ) {
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

            //Progress bar
            Column(
                modifier = Modifier
                    //.padding(16.dp)
                    //.background(Color(0xFFEFB8C8))
            ) {
                Text(
                    text = "Progress: $checkedTasks / ${tasks.size}",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(10.dp)

                )
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    color = Color(0xFF7D5260)
                )
            }

            //Scrollable List
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(tasks.size) { index ->
                    val task = tasks[index]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(
                                if (task.second) Color(0xFFCCC2DC) else Color(0xFFD0BCFF),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                tasks[index] = task.copy(second = !task.second)
                            }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = task.second,
                            onCheckedChange = {
                                tasks[index] = task.copy(second = it)
                            }
                        )
                        Text(
                            text = task.first,
                            modifier = Modifier.weight(1f),
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }


        // Footer with buttons
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp) // Footer height
                .align(Alignment.BottomCenter) // Fix the footer at the bottom
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
                // Button 1
                Button(onClick = { /* TODO: Main Page */ }) {
                    Text("Main Page")
                }

                // Button 2
                Button(onClick = { /* TODO: Add Task */ }) {
                    Text("Add Task")
                }

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BeProductiveTheme {
        AppContent()
    }
}