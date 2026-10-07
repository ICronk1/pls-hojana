package com.example.data.api

import android.util.Log
import com.example.BuildConfig
import com.example.config.AppConfig
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class GeminiPart(
    val text: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiContent(
    val role: String? = null,
    val parts: List<GeminiPart>
)

@JsonClass(generateAdapter = true)
data class GeminiRequest(
    val contents: List<GeminiContent>,
    val systemInstruction: GeminiContent? = null
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(
    val content: GeminiContent? = null
)

@JsonClass(generateAdapter = true)
data class GeminiResponse(
    val candidates: List<GeminiCandidate>? = null
)

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

object GeminiClient {
    private const val TAG = "GeminiClient"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val apiService: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiApiService::class.java)
    }

    suspend fun sendMessage(
        conversationHistory: List<Pair<String, String>>, // role ("user" | "model"), text
        userMessage: String,
        moodId: String = "CHAD"
    ): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }

        // Build Gemini conversation contents
        val contentsList = mutableListOf<GeminiContent>()
        for ((role, text) in conversationHistory.takeLast(10)) {
            val geminiRole = if (role == "user") "user" else "model"
            contentsList.add(GeminiContent(role = geminiRole, parts = listOf(GeminiPart(text = text))))
        }
        contentsList.add(GeminiContent(role = "user", parts = listOf(GeminiPart(text = userMessage))))

        val customizedInstruction = AppConfig.ChatbotConfig.buildSystemInstruction(moodId)
        val systemInstruction = GeminiContent(
            parts = listOf(GeminiPart(text = customizedInstruction))
        )

        val request = GeminiRequest(
            contents = contentsList,
            systemInstruction = systemInstruction
        )

        if (!apiKey.isNullOrBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val response = apiService.generateContent(apiKey, request)
                val reply = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                if (!reply.isNullOrBlank()) {
                    return@withContext reply.trim()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Gemini API call failed: ${e.message}", e)
            }
        }

        // Graceful fallback loving study response customized for her active mood
        return@withContext generateLovingFallbackResponse(userMessage, moodId)
    }

    private fun generateLovingFallbackResponse(message: String, moodId: String): String {
        val lower = message.lowercase()
        val mood = AppConfig.ChatbotConfig.getMood(moodId)

        val moodPrefix = when (mood.id) {
            "NAARAZ" -> "Gaurav ki taraf se sorry meri naaraz bhalu! 😤 Lekin padhai me koi excuse nahi, "
            "STRESSED" -> "Pehle ek lambi saans le meri jaan, load kyu le rahi hai bilkul ❤️ "
            "TIRED" -> "Sleepy eyes khol meri bhalu, dekh ekdum short me bata raha hu 😴 "
            "ROMANTIC" -> "Hayee meri romantic rani, pehle ek kissi 👉👈 fir bata kya doubt hai "
            "CONFUSED" -> "Confusion dur karne ke liye tera personal tutor hazir hai! 🤔 "
            else -> ""
        }

        return when {
            lower.contains("stress") || lower.contains("tired") || lower.contains("thak") || lower.contains("pareshan") -> {
                "Arey meri giga chad ridhima, pehle ek lambi gehri saans le ❤️ Itna load kyu le rahi hai? Chal chup chap 5 minute break le, thoda thanda paani pi. Koi bhi exam ya homework teri himmat aur intelligence se bada nahi hai! Mai hu na tere sath, poori huggy aur kissi dunga bas thoda sa relax kar! ✨"
            }
            lower.contains("bio") || lower.contains("biology") || lower.contains("cell") || lower.contains("neet") -> {
                "${moodPrefix}Abe tereko ye nahi aata? Ye dekh kitna easy hai! 🧬 Biology toh apna sabse pyara subject hai. Bas diagram aur flow samajh le, ratta marne ki zarurat hi nahi hai! NEET me AIR 1 chahiye hume, toh bata kaunse step pe phasi hai meri padhai ki bhalu? 👉👈"
            }
            lower.contains("math") || lower.contains("physics") || lower.contains("formula") || lower.contains("calculate") || lower.contains("sum") -> {
                "${moodPrefix}Abe tereko ye nahi aata? Ye dekh kitna easy hai! 📐 Formula ko step-by-step tod: pehle likh 'Given kya hai', fir dekh 'Formula kya lagega', fir values put kar. Tera dimaag Einstein se bhi tez hai meri jaan, bas thoda sa dhyan laga aur solve kar! 💖"
            }
            lower.contains("chem") || lower.contains("chemistry") || lower.contains("reaction") || lower.contains("organic") -> {
                "${moodPrefix}Abe tereko ye nahi aata? Ye dekh kitna easy hai! 🧪 Chemistry ki reaction aur humari chemistry dono super strong hain! Organic me bas electron ka movement dekh, baki sab apne aap set ho jayega. Jldi se equation bata mai solve karwata hu! ✨"
            }
            lower.contains("quiz") || lower.contains("test") -> {
                "${moodPrefix}Haha chal aaja test le leta hu tera! 💡 Ek simple sa rule: jo bhi concept abhi padha hai, usko bas 2 sentences me mereko Hinglish me samjha de. Agar tu simple shabdon me bata payi, matlab concept crystal clear! Chal shuru ho ja meri padhai ki bhalu!"
            }
            else -> {
                "${moodPrefix}Abe tereko ye nahi aata? Ye dekh kitna easy hai! 🌟 Padhai apni jagah hai par tu giga chad hai yeh mat bhool. Har ek question ko mast goofy mood me step-by-step tod denge! Chal bata exact doubt kya hai meri jaan, poora Hinglish me samjha deta hu! ❤️"
            }
        }
    }
}
