package com.example.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.config.AppConfig
import com.example.data.AppPreferences
import com.example.data.api.GeminiClient
import com.example.ui.components.DailyMoodCheckInDialog
import com.example.ui.components.FloatingHeartsCanvas
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CrispWhite
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.RomanticPink
import com.example.ui.theme.SoftCream
import com.example.ui.theme.SubtleGrayText
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.SurfacePinkLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WhiteBackground
import kotlinx.coroutines.launch

data class ChatMessage(
    val id: Long = System.currentTimeMillis(),
    val sender: String, // "user" or "model"
    val text: String,
    val timeFormatted: String = "Now"
)

@Composable
fun ChatbotScreen(
    appPreferences: AppPreferences? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val prefs = appPreferences ?: remember { AppPreferences(context) }
    var currentMoodId by remember { mutableStateOf(prefs.getSelectedMood()) }
    var showMoodDialog by remember { mutableStateOf(!prefs.isMoodCheckedInToday()) }
    val currentMood = AppConfig.ChatbotConfig.getMood(currentMoodId)

    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                sender = "model",
                text = "${currentMood.emoji} [Feeling: ${currentMood.label}]\n\n${currentMood.greeting}"
            )
        )
    }

    var inputText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    fun sendMessage(textToSend: String) {
        if (textToSend.isBlank() || isLoading) return
        val userMsg = textToSend.trim()
        inputText = ""
        messages.add(ChatMessage(sender = "user", text = userMsg))
        isLoading = true

        coroutineScope.launch {
            val history = messages.dropLast(1).map { Pair(it.sender, it.text) }
            val reply = GeminiClient.sendMessage(history, userMsg, currentMoodId)
            messages.add(ChatMessage(sender = "model", text = reply))
            isLoading = false
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(WhiteBackground)
    ) {
        val isTablet = maxWidth >= 720.dp
        val chatMaxWidth = 960.dp

        FloatingHeartsCanvas(particleCount = if (isTablet) 16 else 10, primaryColor = RomanticPink)

        if (isTablet) {
            // TABLET SUPPORTING PANE LAYOUT (Left: AI Profile & Mood & Prompts, Right: Conversation Thread)
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
                    .imePadding(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Left Supporting Pane (280dp)
                Card(
                    modifier = Modifier
                        .width(300.dp)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CrispWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceCardBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        // Tutor Header
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(SurfacePinkLight)
                                    .border(1.dp, SurfaceCardBorder, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "📚", fontSize = 22.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = AppConfig.ChatbotConfig.BOT_NAME,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Target: NEET AIR 1 💖",
                                    fontSize = 11.sp,
                                    color = RomanticPink,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Daily Mood Selector (Tap to switch persona instantly)
                        Text(
                            text = "Daily Mood & Persona",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Adapts greeting & problem solving",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )

                        AppConfig.ChatbotConfig.MOOD_OPTIONS.forEach { mood ->
                            val isSelected = mood.id == currentMoodId
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) SurfacePinkLight else Color.Transparent)
                                    .border(
                                        1.dp,
                                        if (isSelected) RomanticPink.copy(alpha = 0.6f) else BorderSubtle,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable {
                                        if (currentMoodId != mood.id) {
                                            currentMoodId = mood.id
                                            prefs.saveMood(mood.id)
                                            messages.add(
                                                ChatMessage(
                                                    sender = "model",
                                                    text = "${mood.emoji} [Persona tuned: ${mood.label}]\n\n${mood.greeting}"
                                                )
                                            )
                                        }
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                                    .testTag("tablet_mood_${mood.id}"),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = mood.emoji, fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = mood.label,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) RomanticPink else TextPrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Quick Prompts list
                        Text(
                            text = "Quick Doubts & Prompts",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "One-tap ask",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )

                        AppConfig.ChatbotConfig.QUICK_PROMPTS.forEach { prompt ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(SurfacePinkLight)
                                    .border(1.dp, SurfaceCardBorder, RoundedCornerShape(10.dp))
                                    .clickable { sendMessage(prompt) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = prompt,
                                    fontSize = 11.sp,
                                    color = RomanticPink,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // Right Pane: Active Conversation and Input
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CrispWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceCardBorder)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Header Inside Right Pane
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(RomanticPink)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Active Session • ${currentMood.emoji} ${currentMood.label}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                            }

                            IconButton(
                                onClick = {
                                    messages.clear()
                                    val mood = AppConfig.ChatbotConfig.getMood(currentMoodId)
                                    messages.add(
                                        ChatMessage(
                                            sender = "model",
                                            text = "${mood.emoji} [Feeling: ${mood.label}]\n\n${mood.greeting}"
                                        )
                                    )
                                },
                                modifier = Modifier.testTag("chat_clear_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteSweep,
                                    contentDescription = "Clear Chat",
                                    tint = RomanticPink
                                )
                            }
                        }

                        // Chat Messages List
                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(vertical = 12.dp)
                        ) {
                            items(messages, key = { it.id }) { msg ->
                                ChatBubbleItem(message = msg)
                            }

                            if (isLoading) {
                                item {
                                    TypingIndicator()
                                }
                            }
                        }

                        // Bottom Input Bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CrispWhite)
                                .border(1.dp, SurfaceCardBorder)
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = inputText,
                                onValueChange = { inputText = it },
                                placeholder = {
                                    Text("Ask your tutor a question...", fontSize = 14.sp, color = SubtleGrayText)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("chat_input_field"),
                                shape = RoundedCornerShape(22.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary,
                                    focusedContainerColor = SurfacePinkLight,
                                    unfocusedContainerColor = SurfacePinkLight,
                                    focusedBorderColor = RomanticPink,
                                    unfocusedBorderColor = SurfaceCardBorder
                                ),
                                maxLines = 4,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                                keyboardActions = KeyboardActions(onSend = { sendMessage(inputText) })
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            IconButton(
                                onClick = { sendMessage(inputText) },
                                enabled = inputText.isNotBlank() && !isLoading,
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (inputText.isNotBlank() && !isLoading) RomanticPink
                                        else SurfacePinkLight
                                    )
                                    .testTag("chat_send_btn")
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        color = CrispWhite,
                                        strokeWidth = 2.dp,
                                        modifier = Modifier.size(18.dp)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Send,
                                        contentDescription = "Send",
                                        tint = if (inputText.isNotBlank()) Color.White else SubtleGrayText,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // PHONE SINGLE-COLUMN LAYOUT
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = chatMaxWidth)
                    .align(Alignment.TopCenter)
                    .imePadding()
            ) {
                // Header Bar inside Chat
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(SurfacePinkLight)
                                .border(1.dp, SurfaceCardBorder, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "📚", fontSize = 17.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = AppConfig.ChatbotConfig.BOT_NAME,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(RomanticPink)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "Academic & Study Assistant • Hinglish 💖",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }

                    IconButton(
                        onClick = {
                            messages.clear()
                            val mood = AppConfig.ChatbotConfig.getMood(currentMoodId)
                            messages.add(
                                ChatMessage(
                                    sender = "model",
                                    text = "${mood.emoji} [Feeling: ${mood.label}]\n\n${mood.greeting}"
                                )
                            )
                        },
                        modifier = Modifier.testTag("chat_clear_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = "Clear Chat",
                            tint = RomanticPink
                        )
                    }
                }

                // Daily Mood Selector Bar inside Chat
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfacePinkLight)
                            .border(1.dp, SurfaceCardBorder, RoundedCornerShape(12.dp))
                            .clickable { showMoodDialog = true }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                            .testTag("chat_mood_indicator"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = currentMood.emoji, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Mood: ${currentMood.label}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = RomanticPink
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "• Change",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                // Quick Prompt Chips
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(AppConfig.ChatbotConfig.QUICK_PROMPTS) { prompt ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(SurfacePinkLight)
                                .border(1.dp, SurfaceCardBorder, RoundedCornerShape(14.dp))
                                .clickable { sendMessage(prompt) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = prompt,
                                fontSize = 12.sp,
                                color = RomanticPink,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // Chat Messages List
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    items(messages, key = { it.id }) { msg ->
                        ChatBubbleItem(message = msg)
                    }

                    if (isLoading) {
                        item {
                            TypingIndicator()
                        }
                    }
                }

                // Bottom Input Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CrispWhite)
                        .border(1.dp, SurfaceCardBorder)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = {
                            Text("Ask your tutor a question...", fontSize = 14.sp, color = SubtleGrayText)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_input_field"),
                        shape = RoundedCornerShape(22.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = SurfacePinkLight,
                            unfocusedContainerColor = SurfacePinkLight,
                            focusedBorderColor = RomanticPink,
                            unfocusedBorderColor = SurfaceCardBorder
                        ),
                        maxLines = 4,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(onSend = { sendMessage(inputText) })
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = { sendMessage(inputText) },
                        enabled = inputText.isNotBlank() && !isLoading,
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(
                                if (inputText.isNotBlank() && !isLoading) RomanticPink
                                else SurfacePinkLight
                            )
                            .testTag("chat_send_btn")
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = CrispWhite,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(18.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send",
                                tint = if (inputText.isNotBlank()) Color.White else SubtleGrayText,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        if (showMoodDialog) {
            DailyMoodCheckInDialog(
                currentMoodId = currentMoodId,
                onMoodSelected = { newMoodId ->
                    currentMoodId = newMoodId
                    prefs.saveMood(newMoodId)
                    showMoodDialog = false
                    val newMood = AppConfig.ChatbotConfig.getMood(newMoodId)
                    messages.add(
                        ChatMessage(
                            sender = "model",
                            text = "${newMood.emoji} [Mood updated: ${newMood.label}]\n\n${newMood.greeting}"
                        )
                    )
                },
                onDismiss = { showMoodDialog = false }
            )
        }
    }
}

@Composable
fun ChatBubbleItem(message: ChatMessage) {
    val isUser = message.sender == "user"

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(SurfacePinkLight)
                    .border(1.dp, SurfaceCardBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "✦", fontSize = 12.sp, color = RomanticPink)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Box(
            modifier = Modifier
                .widthIn(max = 620.dp)
                .fillMaxWidth(0.82f)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isUser) 16.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 16.dp
                    )
                )
                .background(
                    if (isUser) RomanticPink else CrispWhite
                )
                .border(
                    1.dp,
                    if (isUser) Color.Transparent else SurfaceCardBorder,
                    RoundedCornerShape(16.dp)
                )
                .padding(14.dp)
        ) {
            Text(
                text = message.text,
                fontSize = 14.sp,
                color = if (isUser) Color.White else TextPrimary,
                lineHeight = 21.sp
            )
        }
    }
}

@Composable
fun TypingIndicator() {
    val infiniteTransition = rememberInfiniteTransition(label = "typing")
    val alpha1 by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(600, easing = LinearEasing), RepeatMode.Reverse),
        label = "a1"
    )
    val alpha2 by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(600, delayMillis = 200, easing = LinearEasing),
            RepeatMode.Reverse
        ),
        label = "a2"
    )
    val alpha3 by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(600, delayMillis = 400, easing = LinearEasing),
            RepeatMode.Reverse
        ),
        label = "a3"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(SurfacePinkLight)
            .border(1.dp, SurfaceCardBorder, RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(text = "Synthesizing response", fontSize = 12.sp, color = TextSecondary)
        Spacer(modifier = Modifier.width(8.dp))
        Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(RomanticPink.copy(alpha = alpha1)))
        Spacer(modifier = Modifier.width(4.dp))
        Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(RomanticPink.copy(alpha = alpha2)))
        Spacer(modifier = Modifier.width(4.dp))
        Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(RomanticPink.copy(alpha = alpha3)))
    }
}

@Preview(name = "Phone - Chatbot", showBackground = true, device = Devices.PIXEL_7)
@Preview(name = "Tablet - Chatbot", showBackground = true, device = Devices.TABLET)
@Composable
fun ChatbotScreenPreview() {
    MyApplicationTheme {
        ChatbotScreen()
    }
}
