package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.model.Note
import com.example.todoapp.viewModel.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_add_edit_note.*
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddEditNoteActivity : AppCompatActivity() {

    private val viewModels by viewModels<NotesViewModel>()

    private var noteId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)


        val noteType = intent.getStringExtra("NoteType")

        if (noteType == "Edit") {
            val noteTitle = intent.getStringExtra("NoteTitle")
            val noteDescription = intent.getStringExtra("NoteDescription")
            noteId = intent.getIntExtra("NoteId", -1)
            addUpdateButton.text = "Update Note"
            editNoteTitle.setText(noteTitle)
            editNoteDescription.setText(noteDescription)
        } else {
            addUpdateButton.text = "Save Note"
        }
        addUpdateButton.setOnClickListener {
            val noteTitle = editNoteTitle.text.toString()
            val noteDescription = editNoteDescription.text.toString()

            if (noteType == "Edit") {
                if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()) {
                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                    val currentDate: String = sdf.format(Date())
                    val updateNote = Note(noteTitle, noteDescription, currentDate)
                    updateNote.id = noteId
                    viewModels.upDateNote(updateNote)
                    Toast.makeText(this, "Note Updated..", Toast.LENGTH_LONG).show()
                }
            } else {
                if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()) {
                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                    val currentDate: String = sdf.format(Date())
                    viewModels.addNote(Note(noteTitle, noteDescription, currentDate))
                    Toast.makeText(this, "Note Added..", Toast.LENGTH_LONG).show()
                }
            }
            startActivity(Intent(applicationContext, TodoActivity::class.java))
            this.finish()
        }
    }
}