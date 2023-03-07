package com.example.todoapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.model.Note
import kotlinx.android.synthetic.main.note_rv_items.view.*

class NoteAdapter(
    private val noteClickDeleteInterface: NoteClickDeleteInterface,
    private val noteClickInterface: NoteClickInterface,
) : ListAdapter<Note, NoteAdapter.MyViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.note_rv_items, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.initialize(currentItem)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val noteDeleteImageView: ImageView = itemView.noteDelete
        fun initialize(note: Note) {
            itemView.apply {
                noteTitle.text = note.noteTitle
                noteTimeStamp.text = note.timeStamp

                noteDeleteImageView.setOnClickListener {

                    noteClickDeleteInterface.onDeleteIconClick(note)
                }

                itemView.setOnClickListener {
                    noteClickInterface.onNoteClick(note)
                }
            }
        }

    }

    interface NoteClickDeleteInterface {
        fun onDeleteIconClick(note: Note)
    }

    interface NoteClickInterface {
        fun onNoteClick(note: Note)
    }


    companion object {

        val diffUtil = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem == newItem
            }
        }

    }

}