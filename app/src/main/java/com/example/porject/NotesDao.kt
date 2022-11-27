package com.example.porject

import androidx.lifecycle.LiveData
import androidx.room.*
import com.airbnb.lottie.L
import java.util.concurrent.Flow

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("Select * from notesTable order by id ASC")
    fun getAllNotes() : LiveData<List<Note>>

    @Query("SELECT * FROM notesTable WHERE title LIKE :searchQuery OR description LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): kotlinx.coroutines.flow.Flow<List<Note>>
}