package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.config.AppConfig
import com.example.data.AppPreferences
import com.example.ui.components.DailyMoodCheckInDialog
import com.example.ui.components.FloatingHeartsCanvas
import com.example.ui.components.RomanticFooter
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CrispWhite
import com.example.ui.theme.KeyGold
import com.example.ui.theme.MyApplicationTheme
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

data class CentreCardInfo(
    val route: String,
    val title: String,
    val subtitle: String,
    val iconEmoji: String,
    val badge: String? = null,
    val accentColor: Color = RomanticPink,
    val isLockedInitially: Boolean = false
)

@Composable
fun HomeScreen(
    appPreferences: AppPreferences,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isPromiseMadeToday = appPreferences.isPromiseMadeToday()
    val streak = appPreferences.getCurrentStreak()

    var currentMoodId by remember { mutableStateOf(appPreferences.getSelectedMood()) }
    var isMoodCheckedInToday by remember { mutableStateOf(appPreferences.isMoodCheckedInToday()) }
    var showMoodDialog by remember { mutableStateOf(false) }
    val currentMoodOption = AppConfig.ChatbotConfig.getMood(currentMoodId)

    val centres = listOf(
        CentreCardInfo(
            route = "chatbot",
            title = "Homework & Study Help",
            subtitle = "RidhimaSaurasAI • Tuned to ${currentMoodOption.emoji} ${currentMoodOption.label}",
            iconEmoji = "📚",
            badge = "RidhimaSaurasAI",
            accentColor = RomanticPink
        ),
        CentreCardInfo(
            route = "meditation",
            title = "Meditation Centre",
            subtitle = "Sorry if i made you mad, here come relax",
            iconEmoji = "🌸",
            badge = "Focus",
            accentColor = RomanticPink
        ),
        CentreCardInfo(
            route = "flappy_love",
            title = "Flappy Box",
            subtitle = "Custom photo box arcade • Tap to jump through the pipes",
            iconEmoji = "📦",
            badge = "Arcade",
            accentColor = RomanticPink
        ),
        CentreCardInfo(
            route = "secret_centre",
            title = "Secret Centre",
            subtitle = "Protected Vault • 26 Confidential Confessions",
            iconEmoji = "🔒",
            badge = "Encrypted",
            accentColor = RomanticPink,
            isLockedInitially = true
        ),
        CentreCardInfo(
            route = "catch_faces",
            title = "Catch My Faces",
            subtitle = "Precision reaction exercise • 650ms peek reflex",
            iconEmoji = "✨",
            badge = "Reaction",
            accentColor = RomanticPink
        ),
        CentreCardInfo(
            route = "sorry_centre",
            title = "Sorry Centre",
            subtitle = "Sincere personal accountability & reconciliation notes",
            iconEmoji = "🕊️",
            badge = "Encrypted",
            accentColor = RomanticPink,
            isLockedInitially = true
        ),
        CentreCardInfo(
            route = "promise_centre",
            title = "Promise Centre",
            subtitle = "5 Lifelong Vows • Daily Consistency Streak",
            iconEmoji = "💍",
            badge = if (isPromiseMadeToday) "$streak Days Active" else "Pending Daily Action",
            accentColor = if (isPromiseMadeToday) RomanticPink else KeyGold
        ),
        CentreCardInfo(
            route = "memories_centre",
            title = "Our Memories",
            subtitle = "Exclusive Photo Album Lane • 49 Captured Moments",
            iconEmoji = "📸",
            badge = "Album",
            accentColor = RomanticPink
        ),
        CentreCardInfo(
            route = "problem_centre",
            title = "Problem Centre",
            subtitle = "Problems you don't wanna share to me? Here you go, its your personal journal",
            iconEmoji = "📓",
            badge = "Private",
            accentColor = RomanticPink
        )
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(WhiteBackground)
    ) {
        val isTablet = maxWidth >= 600.dp
        val columns = when {
            maxWidth >= 1250.dp -> 4
            maxWidth >= 880.dp -> 3
            maxWidth >= 580.dp -> 2
            else -> 1
        }
        val horizontalPadding = when {
            maxWidth >= 900.dp -> 32.dp
            maxWidth >= 600.dp -> 24.dp
            else -> 16.dp
        }

        FloatingHeartsCanvas(particleCount = if (isTablet) 18 else 14, primaryColor = RomanticPink)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 28.dp)
        ) {
            // HERO HEADER
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding, vertical = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isTablet) {
                        // High-Resolution Tablet Hero Banner
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 20.dp)
                                .testTag("home_tablet_hero_banner"),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = CrispWhite),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceCardBorder)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_tablet_banner),
                                    contentDescription = "Tablet Banner",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(
                                                    Color.Transparent,
                                                    Color(0xB3FFFFFF),
                                                    Color(0xF5FFFFFF)
                                                ),
                                                startY = 50f
                                            )
                                        )
                                        .padding(20.dp),
                                    contentAlignment = Alignment.BottomStart
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.Bottom
                                    ) {
                                        Column(modifier = Modifier.weight(1f, fill = false)) {
                                            Text(
                                                text = AppConfig.WELCOME_TITLE,
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = RomanticPink
                                            )
                                            Text(
                                                text = AppConfig.WELCOME_SUBTITLE,
                                                fontSize = 12.sp,
                                                color = TextSecondary,
                                                maxLines = 2,
                                                lineHeight = 16.sp
                                            )
                                        }

                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(SurfacePinkLight)
                                                    .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
                                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                                            ) {
                                                Text(
                                                    text = "🔥 $streak Days Streak",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = RomanticPink
                                                )
                                            }
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(SurfacePinkLight)
                                                    .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
                                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                                            ) {
                                                Text(
                                                    text = "💖 AI Tutor Active",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = RomanticPink
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(SurfacePinkLight)
                                .border(1.5.dp, SurfaceCardBorder, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "💖", fontSize = 32.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = AppConfig.WELCOME_TITLE,
                        fontSize = if (isTablet) 26.sp else 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = AppConfig.WELCOME_SUBTITLE,
                        fontSize = if (isTablet) 14.sp else 13.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(top = 6.dp, bottom = 18.dp)
                    )

                    if (isTablet) {
                        // Tablet / Wide: Side-by-side Promise Banner & Mood Banner
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            DailyPromiseBanner(
                                isPromiseMadeToday = isPromiseMadeToday,
                                streak = streak,
                                onNavigate = onNavigate,
                                modifier = Modifier.weight(1f)
                            )

                            DailyMoodCheckInBanner(
                                isMoodCheckedInToday = isMoodCheckedInToday,
                                currentMoodOption = currentMoodOption,
                                onClick = { showMoodDialog = true },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    } else {
                        // Phone: Stacked vertically
                        DailyPromiseBanner(
                            isPromiseMadeToday = isPromiseMadeToday,
                            streak = streak,
                            onNavigate = onNavigate,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        DailyMoodCheckInBanner(
                            isMoodCheckedInToday = isMoodCheckedInToday,
                            currentMoodOption = currentMoodOption,
                            onClick = { showMoodDialog = true },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // CENTRES SECTION TITLE
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Centres & Modules",
                        fontSize = if (isTablet) 18.sp else 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "${centres.size} Available",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }

            // CENTRES CARDS (Multi-Column structure with no letterboxing)
            if (columns > 1) {
                val chunkedCentres = centres.chunked(columns)
                items(chunkedCentres.size) { rowIndex ->
                    val rowItems = chunkedCentres[rowIndex]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = horizontalPadding, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        for (centre in rowItems) {
                            CentreCard(
                                centre = centre,
                                onNavigate = onNavigate,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        repeat(columns - rowItems.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            } else {
                items(centres.size) { index ->
                    val centre = centres[index]
                    CentreCard(
                        centre = centre,
                        onNavigate = onNavigate,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = horizontalPadding, vertical = 5.dp)
                    )
                }
            }

            // PERSISTENT FOOTER (Full width padding)
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding)
                ) {
                    RomanticFooter()
                }
            }
        }

        if (showMoodDialog) {
            DailyMoodCheckInDialog(
                currentMoodId = currentMoodId,
                onMoodSelected = { newMoodId ->
                    currentMoodId = newMoodId
                    appPreferences.saveMood(newMoodId)
                    isMoodCheckedInToday = true
                    showMoodDialog = false
                },
                onDismiss = { showMoodDialog = false }
            )
        }
    }
}

@Composable
private fun DailyPromiseBanner(
    isPromiseMadeToday: Boolean,
    streak: Int,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onNavigate("promise_centre") }
            .testTag("home_promise_banner"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CrispWhite),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isPromiseMadeToday) SurfaceCardBorder else RomanticPink.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(SurfacePinkLight)
                        .border(1.dp, BorderSubtle, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isPromiseMadeToday) "✓" else "!",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isPromiseMadeToday) RomanticPink else KeyGold
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (isPromiseMadeToday)
                            "Daily Vow Confirmed ($streak Day Streak)"
                        else
                            "Daily Vow Pending Action",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = if (isPromiseMadeToday)
                            "View commitments and streak records"
                        else
                            "Confirm today's commitment in Promise Centre",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Go",
                tint = RomanticPink
            )
        }
    }
}

@Composable
private fun DailyMoodCheckInBanner(
    isMoodCheckedInToday: Boolean,
    currentMoodOption: AppConfig.ChatbotConfig.MoodOption,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable(onClick = onClick)
            .testTag("home_mood_banner"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CrispWhite),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isMoodCheckedInToday) SurfaceCardBorder else RomanticPink.copy(alpha = 0.6f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(SurfacePinkLight)
                        .border(1.dp, BorderSubtle, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentMoodOption.emoji,
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (isMoodCheckedInToday)
                            "Mood Today: ${currentMoodOption.label}"
                        else
                            "Daily Mood Check-in 💖",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = if (isMoodCheckedInToday)
                            "RidhimaSaurasAI tuned • Tap to change"
                        else
                            "Select your mood to customize AI",
                        fontSize = 11.sp,
                        color = if (isMoodCheckedInToday) TextSecondary else RomanticPink
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Change Mood",
                tint = RomanticPink,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun CentreCard(
    centre: CentreCardInfo,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onNavigate(centre.route) }
            .testTag("centre_card_${centre.route}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CrispWhite),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            SurfaceCardBorder
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(SurfacePinkLight)
                    .border(1.dp, BorderSubtle, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = centre.iconEmoji, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = centre.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )

                    if (centre.isLockedInitially) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Locked",
                            tint = RomanticPink,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = centre.subtitle,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp
                )
            }

            if (centre.badge != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfacePinkLight)
                        .border(1.dp, BorderSubtle, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = centre.badge,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = RomanticPink
                    )
                }
            }
        }
    }
}

@Preview(name = "Phone - Home Screen", showBackground = true, device = Devices.PIXEL_7)
@Preview(name = "Tablet - Home Screen", showBackground = true, device = Devices.TABLET)
@Composable
fun HomeScreenPreview() {
    MyApplicationTheme {
        HomeScreen(
            appPreferences = AppPreferences(LocalContext.current),
            onNavigate = {}
        )
    }
}
