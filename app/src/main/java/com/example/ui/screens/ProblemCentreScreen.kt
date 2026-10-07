package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.config.AppConfig
import com.example.data.db.DiaryDao
import com.example.data.db.DiaryNote
import com.example.ui.components.FloatingHeartsCanvas
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CrispWhite
import com.example.ui.theme.RomanticPink
import com.example.ui.theme.SoftCream
import com.example.ui.theme.SoftPink
import com.example.ui.theme.SubtleGrayText
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.SurfacePinkLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WhiteBackground
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProblemCentreScreen(
    diaryDao: DiaryDao,
    modifier: Modifier = Modifier
) {
    val notes by diaryDao.getAllNotesFlow().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    var showAddDialog by remember { mutableStateOf(false) }
    var noteTitle by remember { mutableStateOf("") }
    var noteContent by remember { mutableStateOf("") }

    val dateFormat = remember { SimpleDateFormat("MMMM d, yyyy • h:mm a", Locale.getDefault()) }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(WhiteBackground)
    ) {
        val noteColumns = when {
            maxWidth >= 1080.dp -> 3
            maxWidth >= 640.dp -> 2
            else -> 1
        }
        val horizontalPadding = if (maxWidth >= 720.dp) 24.dp else 16.dp

        FloatingHeartsCanvas(particleCount = if (noteColumns > 1) 16 else 10, primaryColor = RomanticPink)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = horizontalPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Icon Header
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(SurfacePinkLight)
                    .border(1.dp, SurfaceCardBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "📓", fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Problem Centre",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            // THE EXACT PROMINENT QUOTE
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfacePinkLight),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceCardBorder)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "✦", fontSize = 16.sp, color = RomanticPink)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "\"${AppConfig.PROBLEM_CENTRE_HEADER_QUOTE}\"",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = TextPrimary,
                        lineHeight = 18.sp
                    )
                }
            }

            // NOTES LIST
            if (notes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "✍️", fontSize = 30.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Your personal confidential journal.\nSelect '+' to write down anything on your mind.",
                            fontSize = 13.sp,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    if (noteColumns > 1) {
                        val chunkedNotes = notes.chunked(noteColumns)
                        items(chunkedNotes.size) { rowIndex ->
                            val rowNotes = chunkedNotes[rowIndex]
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                for (note in rowNotes) {
                                    Card(
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("diary_note_${note.id}"),
                                        shape = RoundedCornerShape(14.dp),
                                        colors = CardDefaults.cardColors(containerColor = CrispWhite),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceCardBorder)
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = note.title,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = TextPrimary
                                                )
                                                IconButton(
                                                    onClick = {
                                                        scope.launch { diaryDao.deleteNote(note) }
                                                    },
                                                    modifier = Modifier.size(32.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Delete,
                                                        contentDescription = "Delete",
                                                        tint = RomanticPink,
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                }
                                            }

                                            Text(
                                                text = note.dateFormatted,
                                                fontSize = 11.sp,
                                                color = TextSecondary,
                                                modifier = Modifier.padding(bottom = 8.dp)
                                            )

                                            // Lined Notebook Body
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(SurfacePinkLight, RoundedCornerShape(10.dp))
                                                    .border(1.dp, BorderSubtle, RoundedCornerShape(10.dp))
                                                    .padding(12.dp)
                                            ) {
                                                Text(
                                                    text = note.content,
                                                    fontSize = 13.sp,
                                                    color = TextPrimary,
                                                    lineHeight = 20.sp
                                                )
                                            }
                                        }
                                    }
                                }
                                repeat(noteColumns - rowNotes.size) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    } else {
                        items(notes, key = { it.id }) { note ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("diary_note_${note.id}"),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = CrispWhite),
                                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceCardBorder)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = note.title,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextPrimary
                                        )
                                        IconButton(
                                            onClick = {
                                                scope.launch { diaryDao.deleteNote(note) }
                                            },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete",
                                                tint = RomanticPink,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }

                                    Text(
                                        text = note.dateFormatted,
                                        fontSize = 11.sp,
                                        color = TextSecondary,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )

                                    // Lined Notebook Body
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(SurfacePinkLight, RoundedCornerShape(10.dp))
                                            .border(1.dp, BorderSubtle, RoundedCornerShape(10.dp))
                                            .padding(12.dp)
                                    ) {
                                        Text(
                                            text = note.content,
                                            fontSize = 13.sp,
                                            color = TextPrimary,
                                            lineHeight = 20.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // FAB to add new note
        FloatingActionButton(
            onClick = {
                noteTitle = ""
                noteContent = ""
                showAddDialog = true
            },
            containerColor = RomanticPink,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .testTag("add_diary_note_fab")
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Write Note", tint = Color.White)
        }

        // ADD NOTE DIALOG
        if (showAddDialog) {
            Dialog(onDismissRequest = { showAddDialog = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CrispWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceCardBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "New Reflection Entry",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = noteTitle,
                            onValueChange = { noteTitle = it },
                            placeholder = { Text("Entry Title...", color = SubtleGrayText) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("diary_input_title"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedContainerColor = SurfacePinkLight,
                                unfocusedContainerColor = SurfacePinkLight,
                                focusedBorderColor = RomanticPink,
                                unfocusedBorderColor = SurfaceCardBorder
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = noteContent,
                            onValueChange = { noteContent = it },
                            placeholder = { Text("Write your unfiltered thoughts and reflections...", color = SubtleGrayText, fontSize = 13.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .testTag("diary_input_content"),
                            shape = RoundedCornerShape(12.dp),
                            maxLines = 8,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedContainerColor = SurfacePinkLight,
                                unfocusedContainerColor = SurfacePinkLight,
                                focusedBorderColor = RomanticPink,
                                unfocusedBorderColor = SurfaceCardBorder
                            )
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = { showAddDialog = false },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                            ) {
                                Text("Cancel", color = TextSecondary)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    if (noteTitle.isNotBlank() && noteContent.isNotBlank()) {
                                        val now = Date()
                                        val newNote = DiaryNote(
                                            title = noteTitle.trim(),
                                            content = noteContent.trim(),
                                            dateFormatted = dateFormat.format(now),
                                            timestamp = now.time
                                        )
                                        scope.launch { diaryDao.insertNote(newNote) }
                                        showAddDialog = false
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = RomanticPink, contentColor = Color.White),
                                shape = RoundedCornerShape(12.dp),
                                enabled = noteTitle.isNotBlank() && noteContent.isNotBlank(),
                                modifier = Modifier.testTag("diary_save_btn")
                            ) {
                                Text("Save Entry", color = Color.White, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }
    }
}
