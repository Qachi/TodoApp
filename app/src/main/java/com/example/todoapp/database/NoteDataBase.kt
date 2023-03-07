package com.example.todoapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todoapp.dao.NoteDao
import com.example.todoapp.model.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)

abstract class NoteDataBase : RoomDatabase() {

    abstract fun getNotesDao(): NoteDao

}