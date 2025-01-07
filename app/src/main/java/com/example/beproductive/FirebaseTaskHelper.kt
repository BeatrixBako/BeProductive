package com.example.beproductive

import android.util.Log
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object FirebaseTaskHelper {
    private val database = Firebase.database("https://beproductive-f7dd2-default-rtdb.europe-west1.firebasedatabase.app/")
    private val tasksRef = database.getReference("tasks")

    //Adds a task to firebase and sets isSelected to false
    fun addTask(taskName: String) {
        val taskId = tasksRef.push().key // Generate a unique ID
        val newTask = Task(id = taskId ?: "", name = taskName, isSelected = false)

        tasksRef.child(taskId ?: "").setValue(newTask)
            .addOnSuccessListener {
                Log.d("Firebase", "Task '$taskName' added successfully")
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Error adding task '$taskName'", e)
            }
    }


    //Updates the status of a task in firebase based on id
    fun updateTaskStatus(taskId: String, isSelected: Boolean) {
        tasksRef.child(taskId).child("selected").setValue(isSelected)
            .addOnSuccessListener {
                Log.d("Firebase", "Task status updated successfully")
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Error updating task status", e)
            }
    }

    //Deletes a task from firebade based on id
    fun deleteTask(taskId: String) {
        tasksRef.child(taskId).removeValue()
            .addOnSuccessListener {
                Log.d("Firebase", "Task deleted successfully")
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Error deleting task", e)
            }
    }

}
