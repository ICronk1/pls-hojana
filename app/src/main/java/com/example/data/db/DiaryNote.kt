package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diary_notes")
data class DiaryNote(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val dateFormatted: String,
    val timestamp: Long = System.currentTimeMillis(),
    val moodEmoji: String = "🌸"
)
