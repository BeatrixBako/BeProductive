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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // Main Content: Header and Scrollable List
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

            ScrollableList()
        }


        // Footer with buttons
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp) // Footer height
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


@Composable
fun ScrollableList() {
    val listOfTasks = remember {
        listOf(
            "Task 1", "Task 2", "Task 3", "Task 4", "Task 5", "Task 6", "Task 7", "Task 8", "Task 9",
            "Task 10", "Task 11", "Task 12", "Task 13", "Task 14", "Task 15", "Task 16", "Task 17", "Task 18",
            "Task 19", "Task 20", "Task 21", "Task 22", "Task 23", "Task 24", "Task 25", "Task 26", "Task 27",
            "Task 28", "Task 29", "Task 30"
        )
    }

    //val tasksListViewModel: TasksListViewModel = viewModel()
    //val listOfTasks = tasksListViewModel.tasks.collectAsState()

    var checkedTasks by remember { mutableStateOf(setOf<String>()) }


    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(listOfTasks) { task ->
            ListItem(
                text = task,
                isSelected = checkedTasks.contains(task),
                onSelect = {
                    checkedTasks = if (checkedTasks.contains(task)) {
                        checkedTasks - task  // Remove from selected
                    } else {
                        checkedTasks + task  // Add to selected
                    }
                }
            )
        }
    }
}

@Composable
fun ListItem(
    text: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .height(60.dp)
            .background(
                if (isSelected) Color(0xFFCCC2DC) // Grey background for selected tasks
                else Color(0xFFD0BCFF)    // Purple background for unselected tasks
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = { onSelect() }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(8.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BeProductiveTheme {
        AppContent()
    }
}