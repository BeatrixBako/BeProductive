package com.example.beproductive

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScrollableList(tasks: List<Task>,
                   onTaskStatusChange: (String, Boolean) -> Unit)
        /*(tasks: MutableList<Task>)*/{
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
    ) {
        itemsIndexed(tasks) { index, task ->
            ListItem(
                task = task,
                onTaskStatusChange = { isSelected ->
                    onTaskStatusChange(task.id, isSelected)
                }
            )
        }
        /*
         itemsIndexed(tasks) { index, task ->
            ListItem(
                task = task,
                onCheckedChange = { isChecked ->
                    tasks[index] = task.copy(isSelected = isChecked)
                }
            )
        }*/
    }
}