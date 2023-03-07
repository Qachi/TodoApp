package com.example.todoapp.repo

import com.example.todoapp.model.Note
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

interface TodoRepository {


    suspend fun insertNote(note: Note)

    suspend fun updateNote(note: Note)

    suspend fun deleteNote(note: Note)

    fun getAllNotes(): Flow<List<Note>>

    suspend fun deleteAllNote()

    fun searchDatabase(searchQuery: String): Flow<List<Note>>
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindRepository(repositoryImpl: TodoRepositoryImpl): TodoRepository
}