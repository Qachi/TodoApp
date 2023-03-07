package com.example.todoapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.Note
import com.example.todoapp.repo.TodoRepository
import com.example.todoapp.util.Constant.dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val todoRepository: TodoRepository
):ViewModel() {


    private val searchQuery = MutableStateFlow("")

    private val noteFlow = searchQuery.flatMapLatest {
        todoRepository.getAllNotes()

    }

    val getAllNoteUpdated = noteFlow.asLiveData()

    private val noteEventChannel = Channel<NotesEvent>()
    val noteEvent = noteEventChannel.receiveAsFlow()

    fun deleteNote(note: Note) {
        viewModelScope.launch(dispatcher) {
            todoRepository.deleteNote(note)
        }
    }

    fun upDateNote(note: Note) {
        viewModelScope.launch(dispatcher) {
            todoRepository.updateNote(note)
        }
    }

    fun addNote(note: Note) {
        viewModelScope.launch(dispatcher) {
            todoRepository.insertNote(note)
        }
    }

    fun onTaskSwiped(note: Note) =
        viewModelScope.launch(dispatcher) {
            todoRepository.deleteNote(note)
            noteEventChannel.send(NotesEvent.ShowUndoDeleteNoteMessage(note))

        }

    fun onUndoDeleteClick(note: Note) {
        viewModelScope.launch(dispatcher) {
            todoRepository.insertNote(note)
        }

    }

    fun onDeleteAllTask() = viewModelScope.launch(dispatcher) {
        todoRepository.deleteAllNote()
        noteEventChannel.send(NotesEvent.DeleteAllScreen)

    }

    fun searchDatabase(searchQuery: String): LiveData<List<Note>> {
        return todoRepository.searchDatabase(searchQuery).asLiveData()
    }

    sealed class NotesEvent {
        data class ShowUndoDeleteNoteMessage(val note: Note) : NotesEvent()
        object DeleteAllScreen : NotesEvent()
    }

}

