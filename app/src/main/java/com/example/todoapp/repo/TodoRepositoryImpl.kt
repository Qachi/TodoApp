package com.example.todoapp.repo

import com.example.todoapp.database.NoteDataBase
import com.example.todoapp.model.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(private val noteDatabase: NoteDataBase) : TodoRepository {

    override suspend fun insertNote(note: Note) {
        noteDatabase.getNotesDao().insertNote(note)
    }

    override suspend fun updateNote(note: Note) {
        noteDatabase.getNotesDao().updateNote(note)
    }

    override suspend fun deleteNote(note: Note) {
       noteDatabase.getNotesDao().deleteNote(note)
    }

    override fun getAllNotes(): Flow<List<Note>> {
        return noteDatabase.getNotesDao().getAllNotes()
    }

    override suspend fun deleteAllNote() {
        return noteDatabase.getNotesDao().deleteAllNote()
    }

    override fun searchDatabase(searchQuery: String): Flow<List<Note>> {
        return noteDatabase.getNotesDao().searchDatabase(searchQuery)
    }
}