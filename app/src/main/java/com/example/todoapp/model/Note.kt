package com.example.todoapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="note_table")
data class Note(
    val noteTitle: String,
    val noteDescription: String,
    val timeStamp: String,
    @PrimaryKey(autoGenerate = true) var id : Int = 0
)
