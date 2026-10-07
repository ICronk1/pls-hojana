package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.config.AppConfig
import com.example.data.AppPreferences
import com.example.data.db.AppDatabase
import com.example.ui.components.RomanticTopBar
import com.example.ui.screens.CatchMyFacesScreen
import com.example.ui.screens.ChatbotScreen
import com.example.ui.screens.FlappyLoveScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MeditationScreen
import com.example.ui.screens.MemoriesCentreScreen
import com.example.ui.screens.ProblemCentreScreen
import com.example.ui.screens.PromiseCentreScreen
import com.example.ui.screens.SecretCentreScreen
import com.example.ui.screens.SorryCentreScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.RomanticPink
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.SurfacePinkLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WhiteBackground

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                OurLittleWorldApp()
            }
        }
    }
}

data class NavDestination(
    val route: String,
    val title: String,
    val iconEmoji: String,
    val shortLabel: String
)

val APP_NAV_DESTINATIONS = listOf(
    NavDestination("home", AppConfig.APP_TITLE, "🏠", "Home"),
    NavDestination("chatbot", "Study AI", "📚", "Study AI"),
    NavDestination("meditation", "Meditation", "🌸", "Zen"),
    NavDestination("flappy_love", "Flappy Box", "📦", "Flappy"),
    NavDestination("catch_faces", "Catch Faces", "✨", "Catch"),
    NavDestination("secret_centre", "Secret Vault", "🔒", "Secrets"),
    NavDestination("sorry_centre", "Sorry Centre", "🕊️", "Sorry"),
    NavDestination("promise_centre", "Promises", "💍", "Vows"),
    NavDestination("memories_centre", "Our Journey", "💖", "Memories"),
    NavDestination("problem_centre", "Secret Diary", "📓", "Diary")
)

@Composable
fun OurLittleWorldApp() {
    val context = LocalContext.current
    val appPreferences = remember { AppPreferences(context) }
    val diaryDao = remember { AppDatabase.getInstance(context).diaryDao() }

    var currentScreen by remember { mutableStateOf("home") }

    // Handle system back press
    BackHandler(enabled = currentScreen != "home") {
        currentScreen = "home"
    }

    val screenTitle = when (currentScreen) {
        "home" -> AppConfig.APP_TITLE
        "chatbot" -> "Study Help (RidhimaSaurasAI)"
        "meditation" -> "Meditation"
        "flappy_love" -> "Flappy Box"
        "secret_centre" -> "Secret Vault"
        "catch_faces" -> "Catch My Faces"
        "sorry_centre" -> "Sorry Centre"
        "promise_centre" -> "Promises"
        "memories_centre" -> "Our Journey"
        "problem_centre" -> "Secret Diary"
        else -> AppConfig.APP_TITLE
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteBackground)
    ) {
        // Wide screen / tablet layout (>= 640dp width)
        val isWideScreen = maxWidth >= 640.dp

        Row(modifier = Modifier.fillMaxSize()) {
            // Adaptive Navigation Rail for Wide Screens & Tablets
            if (isWideScreen) {
                NavigationRail(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(96.dp)
                        .background(WhiteBackground)
                        .border(
                            width = 1.dp,
                            color = SurfaceCardBorder,
                            shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
                        )
                        .testTag("app_navigation_rail"),
                    containerColor = WhiteBackground,
                    header = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(top = 16.dp, bottom = 10.dp)
                                .clickable { currentScreen = "home" }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(SurfacePinkLight)
                                    .border(1.dp, RomanticPink.copy(alpha = 0.5f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "💖", fontSize = 22.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "NonChalant Ridhima",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = RomanticPink,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        APP_NAV_DESTINATIONS.forEach { dest ->
                            val selected = currentScreen == dest.route
                            NavigationRailItem(
                                selected = selected,
                                onClick = { currentScreen = dest.route },
                                icon = {
                                    Text(
                                        text = dest.iconEmoji,
                                        fontSize = if (selected) 22.sp else 18.sp
                                    )
                                },
                                label = {
                                    Text(
                                        text = dest.shortLabel,
                                        fontSize = 10.sp,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = RomanticPink,
                                    selectedTextColor = RomanticPink,
                                    unselectedIconColor = TextSecondary,
                                    unselectedTextColor = TextSecondary,
                                    indicatorColor = SurfacePinkLight
                                ),
                                modifier = Modifier
                                    .padding(vertical = 2.dp)
                                    .testTag("nav_rail_${dest.route}")
                            )
                        }
                    }
                }
            }

            // Main Content Area with TopBar
            Scaffold(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(WhiteBackground),
                containerColor = WhiteBackground,
                topBar = {
                    RomanticTopBar(
                        title = screenTitle,
                        canNavigateBack = currentScreen != "home",
                        onBackClick = { currentScreen = "home" }
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Crossfade(
                        targetState = currentScreen,
                        animationSpec = tween(durationMillis = 250),
                        label = "screen_transition"
                    ) { screen ->
                        when (screen) {
                            "home" -> HomeScreen(
                                appPreferences = appPreferences,
                                onNavigate = { dest -> currentScreen = dest }
                            )
                            "chatbot" -> ChatbotScreen(
                                appPreferences = appPreferences
                            )
                            "meditation" -> MeditationScreen()
                            "flappy_love" -> FlappyLoveScreen(
                                appPreferences = appPreferences
                            )
                            "secret_centre" -> SecretCentreScreen(
                                appPreferences = appPreferences
                            )
                            "catch_faces" -> CatchMyFacesScreen(
                                appPreferences = appPreferences
                            )
                            "sorry_centre" -> SorryCentreScreen(
                                appPreferences = appPreferences
                            )
                            "promise_centre" -> PromiseCentreScreen(
                                appPreferences = appPreferences
                            )
                            "memories_centre" -> MemoriesCentreScreen()
                            "problem_centre" -> ProblemCentreScreen(
                                diaryDao = diaryDao
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "Phone Portrait", device = Devices.PIXEL_7, showBackground = true)
@Preview(name = "Phone Landscape", device = "spec:width=891dp,height=411dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape", showBackground = true)
@Preview(name = "Tablet Landscape", device = Devices.TABLET, showBackground = true)
@Preview(name = "Tablet Portrait", device = Devices.NEXUS_9, showBackground = true)
@Composable
fun AppResponsivePreview() {
    MyApplicationTheme {
        OurLittleWorldApp()
    }
}
