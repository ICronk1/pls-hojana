package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao {
    @Query("SELECT * FROM diary_notes ORDER BY timestamp DESC")
    fun getAllNotesFlow(): Flow<List<DiaryNote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: DiaryNote): Long

    @Update
    suspend fun updateNote(note: DiaryNote)

    @Delete
    suspend fun deleteNote(note: DiaryNote)

    @Query("SELECT * FROM diary_notes WHERE id = :id LIMIT 1")
    suspend fun getNoteById(id: Long): DiaryNote?
}
