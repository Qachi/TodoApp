package com.example.todoapp.dao

import androidx.room.*
import com.example.todoapp.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
 interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insertNote(note: Note)

    @Update
    fun updateNote(note: Note)

    @Delete
    fun deleteNote(note: Note)

    @Query("SELECT * FROM note_table ORDER BY id ASC ")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM note_table WHERE noteTitle LIKE '%' || :searchQuery || '%' ORDER BY id ASC")
    fun searchDatabase(searchQuery: String): Flow<List<Note>>


    @Query("DELETE FROM note_table")
    suspend fun deleteAllNote()
}