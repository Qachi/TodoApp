package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.adapter.NoteAdapter
import com.example.todoapp.model.Note
import com.example.todoapp.util.AlertDialog
import com.example.todoapp.viewModel.NotesViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class TodoActivity : AppCompatActivity(), NoteAdapter.NoteClickDeleteInterface,
    NoteAdapter.NoteClickInterface, SearchView.OnQueryTextListener {

    private val viewModels by viewModels<NotesViewModel>()
    private val myAdapter by lazy { NoteAdapter(this, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpRecyclerView()
        initViewModel()
        floatingButton()

        lifecycleScope.launchWhenStarted {
            viewModels.noteEvent.collect { event ->
                when (event) {
                    is NotesViewModel.NotesEvent.ShowUndoDeleteNoteMessage -> {
                        Snackbar.make(
                            findViewById(R.id.relative_layout),
                            "Note Deleted",
                            Snackbar.LENGTH_LONG
                        )
                            .setAction("UNDO") {
                                viewModels.onUndoDeleteClick(event.note)

                            }.show()

                    }

                }
            }
        }
    }

    private fun setUpRecyclerView() {
        recyclerViewNotes.apply {
            adapter = myAdapter
            layoutManager =
                LinearLayoutManager(this@TodoActivity, LinearLayoutManager.VERTICAL, false)
            val divider = DividerItemDecoration(this@TodoActivity, DividerItemDecoration.VERTICAL)
            addItemDecoration(divider)
        }

        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val swipe = myAdapter.currentList[viewHolder.adapterPosition]
                viewModels.onTaskSwiped(swipe)
            }

        }).attachToRecyclerView(recyclerViewNotes)
    }

    private fun initViewModel() {
        lifecycleScope.launchWhenCreated {

            viewModels.getAllNoteUpdated.observe(this@TodoActivity, Observer { list ->

                list?.let {

                    myAdapter.submitList(list)
                }
            })


        }

    }

    private fun floatingButton() {
        fABAddNote.setOnClickListener {
            val intent = Intent(this@TodoActivity, AddEditNoteActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }


    override fun onNoteClick(note: Note) {
        val intent = Intent(this@TodoActivity, AddEditNoteActivity::class.java)
        intent.putExtra("NoteType", "Edit")
        intent.putExtra("NoteTitle", note.noteTitle)
        intent.putExtra("NoteDescription", note.noteDescription)
        intent.putExtra("NoteId", note.id)
        startActivity(intent)
        this.finish()

    }

    override fun onDeleteIconClick(note: Note) {
        AlertDialog.showDialogConfirmation(
            this,
            "Confirmation",
            "Are you sure you want to proceed?"
        ) {
            viewModels.deleteNote(note)
        }
        Toast.makeText(this, "${note.noteTitle} Deleted", Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_fragment, menu)

        val searchItem = menu!!.findItem(R.id.search_View)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

            R.id.deleteAll -> {
                AlertDialog.showDialogConfirmation(
                    this,
                    "Confirm Deletion",
                    "Do you really want to delete all tasks?"
                ) {
                    viewModels.onDeleteAllTask()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchDatabase(newText)

        }
        searchDatabase(newText)
        return true
    }

    private fun searchDatabase(query: String?) {
        val searchQuery = "%$query%"

        viewModels.searchDatabase(searchQuery).observe(this) { list ->
            list.let {
                myAdapter.submitList(list)
            }

        }
    }

}

