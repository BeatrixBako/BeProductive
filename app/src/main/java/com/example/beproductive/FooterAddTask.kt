package com.example.beproductive

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

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
            //Enter new task
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