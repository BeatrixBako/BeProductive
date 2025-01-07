package com.example.beproductive

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ListItem(task: Task, onTaskStatusChange: (Boolean) -> Unit)
        /*(task: Task, onCheckedChange: (Boolean) -> Unit)*/{
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
            onCheckedChange = onTaskStatusChange //onCheckedChange
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = task.name,
            fontSize = 18.sp
        )
    }
}