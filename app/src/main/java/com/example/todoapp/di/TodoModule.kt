package com.example.todoapp.di

import android.content.Context
import androidx.room.Room
import com.example.todoapp.database.NoteDataBase
import com.example.todoapp.dao.NoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
object TodoModule {

    @[Singleton Provides]
    fun provideNoteDatabase(@ApplicationContext context: Context): NoteDataBase =

        Room.databaseBuilder(context, NoteDataBase::class.java, "todo_database")
            .fallbackToDestructiveMigration()
            .build()


    @[Singleton Provides]
    fun provideNoteDao(noteDataBase: NoteDataBase): NoteDao =
        noteDataBase.getNotesDao()
}

