package com.example.beproductive

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
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
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beproductive.ui.theme.BeProductiveTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.serialization.Serializable
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStreamWriter


//import androidx.lifecycle.viewmodel.compose.viewModel
/*
abstract class ReadandWriteSnipets {
    private lateinit var database: DatabaseReference

    fun initializeDbRef() {
        // [START initialize_database_ref]
        database = Firebase.database.reference
        // [END initialize_database_ref]
    }

    fun writeNewTask(taskID: String, name: String, isSelected: Boolean) {
        val task = Task(name, isSelected)

        database.child("tasks").child(taskID).setValue(task)
    }
}
 */

@Serializable
data class Task(
    //var id: String = "",
    var name: String = "",
    var isSelected: Boolean = false
)


/*
//Code for handeling ROOM for Android
@Entity(tableName = "listoftasks",
        indices = [Index(value = ["name"],
        unique = true)])
data class Task(
    @PrimaryKey val name: String,
    @ColumnInfo (name = "isSelected") var isSelected: Boolean = false
)


@Dao
interface TaskDao {
    @Query("SELECT * FROM listoftasks")
    fun getAll(): MutableList<Task>

    @Query("SELECT * FROM listoftasks WHERE name IN (:taskName)")
    fun loadAllByName(taskName: IntArray): List<Task>

    @Query("SELECT * FROM listoftasks WHERE name LIKE :n")
    fun findByName(n: String): Task

    @Insert
    fun insertTask(vararg tasks: Task)

    @Update
    fun updateTask(vararg tasks: Task)

    @Delete
    fun delete(user: Task)
}


@Database(entities = [Task::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
*/


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
        var name: String? = null
        var isSelected: Boolean? = null

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    when (parser.name) {
                        "name" -> name = parser.nextText()
                        "isSelected" -> isSelected = parser.nextText().toBoolean()
                    }
                }
                XmlPullParser.END_TAG -> {
                    if (parser.name == "task" && name != null && isSelected != null) {
                        tasks.add(Task(name, isSelected))
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

object FirebaseTaskHelper {
    private val database = Firebase.database("https://beproductive-f7dd2-default-rtdb.europe-west1.firebasedatabase.app/")
    //private val database = FirebaseDatabase.getInstance("https://beproductive-f7dd2-default-rtdb.europe-west1.firebasedatabase.app/")
    private val tasksRef = database.getReference("tasks")

    //Adds a task to firebase based on name and checked status(false)
    fun addTask(task: Task) {
        val newTaskRef = tasksRef.push()
        newTaskRef.setValue(task)
            .addOnSuccessListener {
                Log.d("Firebase", "Task added successfully")
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Error adding task", e)
            }
    }

    //Gets the Firebase ID of a task based on it's name
    fun getTaskIdByName(taskName: String, onTaskIdFound: (String?) -> Unit) {
        val tasksRef = FirebaseDatabase.getInstance().getReference("tasks")

        tasksRef.orderByChild("name").equalTo(taskName).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Get the first matching task ID
                    val taskId = snapshot.children.firstOrNull()?.key
                    onTaskIdFound(taskId) // Pass the task ID to the callback
                } else {
                    onTaskIdFound(null) // No matching task found
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error querying task by name", error.toException())
                onTaskIdFound(null)
            }
        })
    }


    //Deletes a task from Firebase based on it's ID that will be found by it's name
    fun deleteTaskByName(taskName: String) {
        getTaskIdByName(taskName) { taskId ->
            if (taskId != null) {
                val tasksRef = FirebaseDatabase.getInstance().getReference("tasks")
                tasksRef.child(taskId).removeValue()
                    .addOnSuccessListener {
                        Log.d("Firebase", "Task '$taskName' deleted successfully")
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firebase", "Error deleting task '$taskName'", e)
                    }
            } else {
                Log.d("Firebase", "Task '$taskName' not found")
            }
        }
    }


    fun updateTaskStatusByName(taskName: String, newStatus: Boolean) {
        getTaskIdByName(taskName) { taskId ->
            if (taskId != null) {
                val tasksRef = FirebaseDatabase.getInstance().getReference("tasks")
                tasksRef.child(taskId).child("isSelected").setValue(newStatus)
                    .addOnSuccessListener {
                        Log.d("Firebase", "Task '$taskName' status updated successfully")
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firebase", "Error updating task '$taskName'", e)
                    }
            } else {
                Log.d("Firebase", "Task '$taskName' not found")
            }
        }
    }
}


class MainActivity : ComponentActivity() {
    private val tasks = mutableStateListOf<Task>()

    //private val database = FirebaseDatabase.getInstance("https://beproductive-f7dd2-default-rtdb.europe-west1.firebasedatabase.app/")
    //private val tasksRef = database.getReference("tasks")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Loads all the tasks from the XML file
        tasks.addAll(TaskStorage.loadTasks(this))
        enableEdgeToEdge()
        setContent {
            BeProductiveTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppContent(tasks = tasks, onSaveTasks = { saveTasks() })
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        // Save tasks when the app goes into the background
        saveTasks()
    }

    private fun saveTasks() {
        TaskStorage.saveTasks(this, tasks)
    }
}




@Composable
fun AppContent(tasks: SnapshotStateList<Task>,
               onSaveTasks: () -> Unit) {


    val context = LocalContext.current
    /*val tasks = remember {
        mutableStateListOf<Task>().apply {
            addAll(TaskStorage.loadTasks(context)) // Load tasks at startup
        }
    }*/

    /*
    //Init for no json
    val tasks = remember {
        mutableStateListOf(
            Task("Task1",false),
            Task("Task2",false),
            Task("Task3",false),
            Task("Task4",false),
            Task("Task5",false),
            Task("Task6",false),
            Task("Task7",false),
            Task("Task8",false),
            Task("Task9",false),
            Task("Task10",false)
        )
    }
    */

    /////////////////////////////////////
    /////////////////////////////////////
    /////////////////////////////////////
    /////////////////////////////////////
    /////////////////////////////////////
    /////////////////////////////////////
    /////////////////////////////////////
    /*
    //Room initialization
    val db = Room.databaseBuilder(
        this,
        AppDatabase::class.java, "listoftasks"
    ).build()


    val userDao = db.taskDao()
    val tasks: MutableList<Task> = userDao.getAll()

     */



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

            //Footer with 'New Task' box and buttons
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
                            //aici vine si adaugarea in room
                            /*////////////////////////////////////
                            /////////////////////////////////////
                            /////////////////////////////////////
                            /////////////////////////////////////
                            /////////////////////////////////////
                            /////////////////////////////////////
                             */
                            /*if (newTaskName.text.isNotBlank()) {
                                tasks.add(Task(newTaskName.text))
                                newTaskName = TextFieldValue("")

                                //Add to XML file
                                TaskStorage.saveTasks(context, tasks) // Save tasks

                                //Add to firebase
                                //writeNewTask();
                            }*/

                            if (newTaskName.text.isNotBlank()) {
                                val newTask = Task(name = newTaskName.text)

                                // Add to local list
                                tasks.add(Task(newTaskName.text))

                                // Add to Firebase
                                FirebaseTaskHelper.addTask(newTask)

                                // Clear input
                                newTaskName = TextFieldValue("")

                                //Add to XML file
                                TaskStorage.saveTasks(context, tasks) // Save tasks
                            } else {
                                Toast.makeText(context, "Task name cannot be empty", Toast.LENGTH_SHORT).show()
                            }
                        },
                        onDeleteCompleted = {
                            //aici vine si stergerea din room
                            /*////////////////////////////////////
                            /////////////////////////////////////
                            /////////////////////////////////////
                            /////////////////////////////////////
                            /////////////////////////////////////
                            /////////////////////////////////////
                            /////////////////////////////////////
                             */

                            //Deletes tasks form firebase by id, which is found based on the name of the task
                            tasks.filter { it.isSelected }.forEach{ task ->  
                                FirebaseTaskHelper.deleteTaskByName(task.name)
                            }

                            //Deletes task from local list
                            tasks.removeAll { it.isSelected }

                            onSaveTasks()

                            //Updates XML file
                            TaskStorage.saveTasks(context, tasks) // Save tasks

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