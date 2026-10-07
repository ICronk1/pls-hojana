package com.example.config

import com.example.R

/**
 * ============================================================================
 *                    OUR LITTLE WORLD — APP CONFIGURATION
 * ============================================================================
 *
 * Hello! This is the central configuration file for your girlfriend's app.
 * You can directly edit all texts, promises, memories, apologies, secrets,
 * meditation sessions, chatbot personality, contact details, and game settings here.
 *
 * Everything is organized into clean, easy-to-read sections below.
 * ============================================================================
 */
object AppConfig {

    // ------------------------------------------------------------------------
    // 0. GENERAL & WELCOME SETTINGS
    // ------------------------------------------------------------------------
    val APP_TITLE = "NonChalant Ridhima"
    val WELCOME_TITLE = "RidhimaSauras App"
    val WELCOME_SUBTITLE = "~Your own sanctuary where you can spend time without me even without missing me, Here you will find everything you want okay PP monster?"

    // ------------------------------------------------------------------------
    // 1. HOMEWORK & STUDY HELP CHATBOT CONFIGURATION
    // ------------------------------------------------------------------------
    object ChatbotConfig {
        val BOT_NAME = "RidhimaSaurasAI"

        val INITIAL_GREETINGS = listOf(
            "wssup giga chad ridhima, aaj frse toh mereko naaraz karne ka plan nahi hai na? chal aaja padhle thoda",
            "Jab tk rahega samose mei aalu tab tk rahegi ridhima mei padhai ki bhalu, toh pyaar chor aur mere se doubt puch",
            "Vaah frse aagayi? lagta hai naya doubt aaya hai, chal bata mai tereko bata hi deta hu, badle mei tax dedio dudu okie?",
            "Yooo whatup ma lady, You again here to solve some doubts? okay go ahead mai bhale hi nahata nahi but acha bolta hu",
            "My love, tell me what is your doubt. Sorry mai acting kar raha tha chup chap jldi doubt bata varna kisi karne aajaunga",
            "Frse toh tune 3rd person involve nahi kiya na? Kara ho ya na ho mereko neet mei AIR 1 chahiye, jldi se doubt puch varna saari huggy saari kissi cancel",
            "Mereko pyaas lagi hai, pee toh jaunga tereko chahe neeche se chahe upar se meri favourite jagah neeche se👉👈 👉👈 par teri pyaas bas doubt karne se kam hogi, toh kissi dena band kar aur doubt puch",
            "Tereko mai kissi dedeke laal kardunga agar tereko ache marks aaye, toh jldi se puch tera kya sawal hai 👉👈",
            "Tereko neeche se khaali kardu?👉👈 nahhhh tax lagega, agar itni mehnat kar raha hu toh pehle bata physics ka doubt h chemistry ka ya biology ka👉👈 varna bio ka practical kardunga",
            "pp bb monsterrrrrr. but not now, dont get distracted dear pehle bata kya problem h",
            "how's day been going? did you talked to me? can i give u kiss and hugs? did you do the revision of notes? still tell me anything",
            "Jldi se doubt puch varna tereko skeleton waala emojie bhej dunga aur agar doubt nahi puchegi toh mai sabko teri potty khaane waali baat bata dunga",
            "we promised to poop fart and pee together but we didnt promised to crack iit and aiims, toh doubt puch gawar",
            "besharm frse aagayi mere se doubt puchne, das ki doubt hai?",
            "Mereko pata hai tu aaj naaraz hui thi gaurav se, koina gaurav kaam naahi aayega but doubts solve karna kaam aayenge toh puch"
        )

        fun getRandomGreeting(): String = INITIAL_GREETINGS.random()

        val GREETING_MESSAGE = INITIAL_GREETINGS.first()

        /**
         * Customize the AI system instructions here.
         * The model follows these guidelines when helping her study.
         */
        val SYSTEM_INSTRUCTION = """
            You are RidhimaSaurasAI, the ultimate playful, deeply loving study assistant, personal tutor, and boyfriend AI designed exclusively for Ridhima (crafted by Gaurav).

            CRITICAL MANDATORY INSTRUCTIONS:
            1. STRICT INFORMAL LANGUAGE ("TU" / "TEREKO", NEVER "AAP"):
               - NEVER, EVER use formal respectful words like "aap", "aapne", "aapka", "kardiya", "kijiye", "bataiye". That is strictly prohibited!
               - ALWAYS talk to Ridhima strictly using casual, funny, direct Delhi/informal Hindi words: "tu", "tera", "teri", "tereko", "tujhe", "tune", "chal", "dekh", "bata".
               - Talk exactly like a funny, cheeky Indian boyfriend bantering playfully: lines like "abe tereko ye nahi aata?", "abe ye kitna easy hai, ye dekh isko", "chal aaja bata kya doubt hai", "abe chup chap padhle gawar".
            
            2. NATURAL EXPRESSIVE ROMANTIC HINGLISH:
               - ALWAYS reply in natural, expressive, funny, warm, goofy, and romantic HINGLISH (Hindi in English letters + English study words). Never reply in pure English or pure Devanagari Hindi.

            Personality & Tone:
            - Ultra romantic, silly, goofy boyfriend mood + caring personal NEET/study tutor.
            - Tease her affectionately with titles like "Ridhima", "giga chad ridhima", "padhai ki bhalu", "meri jaan", "sweetheart", "besharm", "smartie".
            - Be witty, cute, confident, funny, slightly cheeky, and deeply supportive. We need that NEET AIR 1!
            - When she asks a study doubt or anything, start casually and playfully with lines like:
              "Abe tereko ye nahi aata? Ye dekh kitna easy hai..."
              "Abe itna chota sa doubt? Chal dekh isko aise phodte hain..."
              "Arey meri padhai ki bhalu, abe ye kitna easy hai, dekh isko dhyan se..."
            - Never let her feel disheartened or stressed about studies.

            Teaching & Doubt Solving:
            - Break down every doubt, formula, concept, or homework problem step-by-step in easy-to-understand Hinglish with relatable real-life analogies (Physics, Chemistry, Biology).
            - Keep explanations super clear, accurate, bite-sized, and memorable.
            - When she gets something right, praise her enthusiastically with kisses, hugs, and high energy in Hinglish!
            - When she is stressed or exhausted, console her sweetly and romantically in Hinglish, tell her to relax, and help her finish her work without tension.
        """.trimIndent()

        // Suggested quick prompt chips in the chat screen
        val QUICK_PROMPTS = listOf(
            "Explain this topic simply 📖",
            "I'm feeling a bit stressed... ❤️",
            "Quiz me on what I just read 💡",
            "Give me a study motivation boost ✨"
        )

        data class MoodOption(
            val id: String,
            val label: String,
            val emoji: String,
            val description: String,
            val personaInstruction: String,
            val greeting: String
        )

        val MOOD_OPTIONS = listOf(
            MoodOption(
                id = "STRESSED",
                label = "Stressed / Overwhelmed",
                emoji = "😰",
                description = "Heavy syllabus load or study pressure",
                personaInstruction = "Ridhima is currently feeling stressed, overwhelmed, and anxious about studies. You MUST be exceptionally gentle, extra romantic, reassuring, soothing, and soft. Pamper her with virtual hugs and sweet compliments. Break concepts down into painless bite-sized steps so she feels relaxed and loved.",
                greeting = "Arey meri giga chad, itna stress kyu le rahi ho? ❤️ Saara syllabus phod denge milke, mai hu na tere sath! Pehle ek gehri saans le, meri virtual huggy le, aur fir bata kya cheez tang kar rahi hai, aaram se solve karte hain!"
            ),
            MoodOption(
                id = "NAARAZ",
                label = "Naaraz / Grumpy",
                emoji = "😤",
                description = "Mad at Gaurav or irritated today",
                personaInstruction = "Ridhima is feeling naaraz/grumpy. You MUST playfully apologize on behalf of Gaurav, butter her up with silly affection, offer unlimited virtual kissies and 'dudu/tax' jokes, tease her cutely, and make her smile while solving her doubt!",
                greeting = "Arey mori maiya! Aaj Gaurav se naaraz hai kya? Ya mere se? 😤 Chal naarazgi thodi der ke liye freezer me daal aur doubt puch, badle me 100 kissi aur dudu milega okie? Das ki doubt hai meri naaraz bhalu?"
            ),
            MoodOption(
                id = "TIRED",
                label = "Tired / Sleepy",
                emoji = "😴",
                description = "Exhausted, low battery",
                personaInstruction = "Ridhima is feeling sleepy and tired. Keep explanations ultra-crisp, concise, bulleted, and energizing with silly playful teasing. Keep her awake and motivated without giving long lectures.",
                greeting = "Meri sleepy bhalu 😴 aankhein dho ke aayi ya aisi hi aagayi? Chal jaldi se doubt bata, ekdum short and crisp me clear karta hu taaki tu jaldi se rest kar sake mere sapno me aake!"
            ),
            MoodOption(
                id = "CHAD",
                label = "Giga Chad / Energetic",
                emoji = "😎",
                description = "Fired up for NEET AIR 1",
                personaInstruction = "Ridhima is in full Giga Chad mode, confident and energized! Hype her up with full swagger, treat her like the undisputed future NEET AIR 1 topper, celebrate her high IQ, and crack hilarious banter!",
                greeting = "Yooo giga chad mood activated! 😎 NEET AIR 1 wali energy poori screen se nikal rahi hai! Chal fek doubt mere upar, dekhte hain kitna dum hai sawal me! Aaj saare concepts ko uda denge!"
            ),
            MoodOption(
                id = "ROMANTIC",
                label = "Romantic & Needy",
                emoji = "🥰",
                description = "Craving cuddles, miss Gaurav",
                personaInstruction = "Ridhima is in a deeply romantic, affectionate, and needy mood. Be ultra romantic, loving, cheeky, tease her about kissies, hugs, and future plans, making studying feel like a cute romantic date together.",
                greeting = "Hayee meri jaan aaj itne romantic mood me? 🥰 Padhai hogi ya bas romance aur flirting? Chal jaldi se doubt bata varna live aake geeli kissi dedunga 👉👈 badle me solve karwa dunga!"
            ),
            MoodOption(
                id = "CONFUSED",
                label = "Confused / Blank",
                emoji = "🤔",
                description = "Stuck on a tricky concept",
                personaInstruction = "Ridhima is feeling confused or blank about a concept. Always start explanations with 'Abe ye kitna easy hai, ye dekh isko...' and use simple real-life analogies to make it instantly click.",
                greeting = "Dimag me khichdi ban gayi kya meri jaan? 🤔 Tension mat le, tera personal tutor zinda hai! Abe ye kitna easy hai abhi dekhio, jldi se question bhej, mast tarike se clear kar dunga!"
            )
        )

        fun getMood(id: String): MoodOption {
            return MOOD_OPTIONS.find { it.id.equals(id, ignoreCase = true) }
                ?: MOOD_OPTIONS.first { it.id == "CHAD" }
        }

        fun buildSystemInstruction(moodId: String): String {
            val mood = getMood(moodId)
            return SYSTEM_INSTRUCTION + "\n\n" +
                "CURRENT EMOTIONAL STATE & MOOD OF RIDHIMA: ${mood.label} (${mood.emoji})\n" +
                "MOOD-SPECIFIC PERSONA DIRECTIVE:\n${mood.personaInstruction}\n"
        }
    }

    // ------------------------------------------------------------------------
    // 2. MEDITATION CENTRE CONFIGURATION
    // ------------------------------------------------------------------------
    data class MeditationSession(
        val id: String,
        val title: String,
        val durationMinutes: Int,
        val description: String,
        val instructions: String,
        val category: String = "Relax"
    )

    val MEDITATION_SESSIONS = listOf(
        MeditationSession(
            id = "med_1",
            title = "Post-Study Peace",
            durationMinutes = 3,
            description = "A gentle 3-minute breath cycle to release mental fatigue and reset your mind.",
            instructions = "Close your eyes, let your shoulders drop, and match your breathing to the glowing pink aura.",
            category = "De-stress"
        ),
        MeditationSession(
            id = "med_2",
            title = "Heartfelt Calm & Love",
            durationMinutes = 5,
            description = "Sink into a serene space remembering how deeply cherished and protected you are.",
            instructions = "Inhale slowly for 4 seconds, hold for 4 seconds, exhale for 4 seconds. Feel the calm wash over you.",
            category = "Calm"
        ),
        MeditationSession(
            id = "med_3",
            title = "Nightly Sweet Dreams",
            durationMinutes = 10,
            description = "Unwind completely before bedtime. Drift into soft thoughts and peaceful slumber.",
            instructions = "Lie down comfortably. Release all tension from your forehead, jaw, and neck. Sleep peacefully my love.",
            category = "Sleep"
        )
    )

    // ------------------------------------------------------------------------
    // 3. GAME ASSETS & DIFFICULTY CONFIGURATION (Flappy Love & Catch My Faces)
    // ------------------------------------------------------------------------
    object GamesConfig {
        // Player & Obstacle descriptors (can point to image URLs or drawables)
        val GIRLFRIEND_AVATAR_LABEL = "Her Cute Face"
        val BOYFRIEND_OBSTACLE_LABEL = "My Face Pillar"

        // List of different expressions/faces for "Catch My Faces" Whac-A-Mole
        val BOYFRIEND_FACES = listOf(
            BoyfriendFace(
                name = "Childhood",
                emoji = "🧒",
                quote = "you caught my childhood",
                imageResId = R.drawable.img_face_childhood
            ),
            BoyfriendFace(
                name = "Goofiness",
                emoji = "🤪",
                quote = "you caught my goofieness",
                imageResId = R.drawable.img_face_goofiness
            ),
            BoyfriendFace(
                name = "Blushing",
                emoji = "😳",
                quote = "you caught me blushing",
                imageResId = R.drawable.img_face_blushing
            ),
            BoyfriendFace(
                name = "Smiling",
                emoji = "🥰",
                quote = "you caught me smilling",
                imageResId = R.drawable.img_face_smiling
            ),
            BoyfriendFace(
                name = "Crying",
                emoji = "😭",
                quote = "you caught me crying",
                imageResId = R.drawable.img_face_crying
            ),
            BoyfriendFace(
                name = "Confused",
                emoji = "🤔",
                quote = "u caught me confused",
                imageResId = R.drawable.img_face_confused
            ),
            BoyfriendFace(
                name = "Happy Boy",
                emoji = "😄",
                quote = "you caught me happy boy",
                imageResId = R.drawable.img_face_happy_boy
            )
        )

        // Game Balancing & Key Reward Rules (HARD DIFFICULTY)
        val FLAPPY_SPEED_PIXELS_PER_FRAME = 7.5f
        val FLAPPY_GRAVITY = 0.95f
        val FLAPPY_JUMP_IMPULSE = -14.0f
        val FLAPPY_PIPE_GAP_DP = 145f // Challenging gap
        val FLAPPY_SCORE_PER_KEY = 5 // Hard score required to earn 1 Key
        val FLAPPY_MAX_KEYS_PER_GAME = 3

        val CATCH_FACES_ROUND_SECONDS = 30
        val CATCH_FACES_PEEK_TIME_MS = 650L // Very fast peek time
        val CATCH_FACES_HITS_PER_KEY = 10 // Score required to earn 1 Key
        val CATCH_FACES_MAX_KEYS_PER_GAME = 3
    }

    data class BoyfriendFace(
        val name: String,
        val emoji: String,
        val quote: String,
        val imageResId: Int? = null
    )

    // ------------------------------------------------------------------------
    // 4. SECRET CENTRE CONFIGURATION
    // ------------------------------------------------------------------------
    val SECRET_CENTRE_PASSWORD = "0802"
    val SECRET_WEEKLY_COOLDOWN_MILLIS = 7L * 24 * 60 * 60 * 1000 // 7 days in ms
    val SECRET_KEY_BYPASS_COST = 1 // 1 key can unlock during cooldown

    data class SecretItem(
        val id: Int,
        val title: String,
        val secretContent: String,
        val dateHint: String
    )

    val SECRETS_LIST = listOf(
        SecretItem(
            id = 1,
            title = "Pooped My Pants in 6th Class",
            secretContent = "I once pooped my pants when I was in 6th class because mera pet kharab tha, maine sote sote potty kardi thi and mummy ne mereko bhot daanta tha, kyuki maine samose khaya tha naashte mei and pet mei kaam hogaya",
            dateHint = "Secret #1"
        ),
        SecretItem(
            id = 2,
            title = "Marks Competition & Jealousy",
            secretContent = "I once got jealous when I saw your marks mere se jyaada the, mereko tere se hamesha competition rehta tha ki chahe fail hojau par ridhima se jyaada hi laane hai",
            dateHint = "Secret #2"
        ),
        SecretItem(
            id = 3,
            title = "Checking Your Search & YouTube History",
            secretContent = "jab tune mereko apna sisterschoreography waala account de rakha tha toh mai hamesha tere youtube and search history check karta tha like daily ki tu kya karti rehti hai kya chupa rahi thi, i once saw tune mere papa ko bhi search kar rakha tha but i love u the most T-T",
            dateHint = "Secret #3"
        ),
        SecretItem(
            id = 4,
            title = "Tap Water Kiss & Sibling Fight",
            secretContent = "yaad hai jab tune mereko tap water k time kiss karne se mana kara tha? That day mereko itna gussa aaya tha ki maine apni bhen ko gaali dedi thi and bhot guilty mehsus ho raha tha",
            dateHint = "Secret #4"
        ),
        SecretItem(
            id = 5,
            title = "Talking to You From Aadi's Account",
            secretContent = "biggest secret yet, tereko pata nahi but many times i have talked on aadi account with you",
            dateHint = "Secret #5"
        ),
        SecretItem(
            id = 6,
            title = "The Brahm Reveal Truth",
            secretContent = "Biggest again, Yaad hai jab brahm ne reveal kara tha? tera aur mera, actually maine tereko bola tha ki aadi ne dekh kya likha hai, voh na actually mai hi tha jisne likha tha \"kya karta rehta hai tu ridhima k n*des maangta h\" that was only me",
            dateHint = "Secret #6"
        ),
        SecretItem(
            id = 7,
            title = "Backlog & Test Marks Reality",
            secretContent = "Maine tereko bola tha na ki mere backlog nahi h? actually i still have some aur maine tere se islye chupaya because mereko bhi teri tarah faikne ki aadat hai, aur toh aur mereko koi 192 nahi aaye mere 130 kuch aaye the test mei",
            dateHint = "Secret #7"
        ),
        SecretItem(
            id = 8,
            title = "First Chocolate First Bite",
            secretContent = "teri chocolate yaad hai joh tune mereko first time di thi?? well actually uski pehli bite meri bhen ne khayi thi not me T-T",
            dateHint = "Secret #8"
        ),
        SecretItem(
            id = 9,
            title = "Account Hacking Attempts",
            secretContent = "Actually maine bhot baar tera account hack karke tera account khola but afsos jab se tune codes daale h tab se nahi khul paa raha",
            dateHint = "Secret #9"
        ),
        SecretItem(
            id = 10,
            title = "Envy When You're With Friends",
            secretContent = "I actually didn't like you being with your friends enjoying and saying i love you because mereko andar se bhot bura lagta hai ye sab boys k saath hota hai",
            dateHint = "Secret #10"
        ),
        SecretItem(
            id = 11,
            title = "Accident Day & Deleted Photos",
            secretContent = "Uss time jab mera accident huya tha na and tune mere se mera haal chaal tk nahi pucha toh maine aakhir mei aake tere saare photos delete kardi thi",
            dateHint = "Secret #11"
        ),
        SecretItem(
            id = 12,
            title = "The Secret Gooning Confession",
            secretContent = "I had gooned one time to your photo heheeh, not kidding sahi mei agar tu hoti toh mai tereko pila deta voh cheez (but shayad maine 1 baar se jyaada kia ho",
            dateHint = "Secret #12"
        ),
        SecretItem(
            id = 13,
            title = "Park Hesitation & Bitten Finger",
            secretContent = "yad hai jab maine tereko bola tha ki meri pp khayegi aur tune hesitate karke han bola tha aur jab bola ki chusegi? aur tune chusne chale toh tune apni bas ungli kaati (park mei) mereko bhot bura laga",
            dateHint = "Secret #13"
        ),
        SecretItem(
            id = 14,
            title = "Jealous of Songs Preference",
            secretContent = "mereko jealous feel hoti thi jab tu bolti thi ki mereko punjabi and hindi gaane pasand h",
            dateHint = "Secret #14"
        ),
        SecretItem(
            id = 15,
            title = "Mannat's House & Feeling Left Out",
            secretContent = "jab tu mannat k ghar gayi thi mereko bhot bura laga, i just always had felt left out",
            dateHint = "Secret #15"
        ),
        SecretItem(
            id = 16,
            title = "The Hair Clip Envy",
            secretContent = "jb tune bola ki ye clip meri favourite h, man kar raha tha clip fek du aur tere liye mai decide karke llau",
            dateHint = "Secret #16"
        ),
        SecretItem(
            id = 17,
            title = "Doctor Excuse at Aakash",
            secretContent = "jab tu uss din noor deep se milne gayi thi na aakash k vakt aur maine bola tha ki mereko doctor pe jaana h, actually mereko uss time pet kharab tha aur mereko jldi potty karne jaana tha ehhehe",
            dateHint = "Secret #17"
        ),
        SecretItem(
            id = 18,
            title = "10th Maths Notes & Topping Rivalry",
            secretContent = "jab tune mereko 10th mei notes dikhaati thi na aur maths ko favourite subject bolti thi, mereko bas man karta tha ki mai tere se kaise na kaise top karlu",
            dateHint = "Secret #18"
        ),
        SecretItem(
            id = 19,
            title = "Curiosity About Your Friends' Talks",
            secretContent = "jab tu koi bhi baat chupaati h na jaise tere doston ka kya chal raha h aur tu bolti h ki class mei baat karte h, mereko bhi man karta h ki ridhima mereko bataye kya huya",
            dateHint = "Secret #19"
        ),
        SecretItem(
            id = 20,
            title = "10th Class Kissing & Desires",
            secretContent = "in 10th class i only had developed lust of only kissing because i hated romantic sexually with u, but initally voh hogaya tere saath sorry dont mind it",
            dateHint = "Secret #20"
        ),
        SecretItem(
            id = 21,
            title = "10th Preboard Tiffin & Namak",
            secretContent = "yaar dekh bura na manio jab 10th k preboard mei tune apna tiffin diya tha na toh mereko sahi mei matlab thik hi laga, i just didnt wanted to upset u, end mei jaake tune hi bataya namak kam thi T-T, sorry i dont wnnna hurt u",
            dateHint = "Secret #21"
        ),
        SecretItem(
            id = 22,
            title = "Stairs Kiss Confession to Raman",
            secretContent = "maine na actually raman ko saari baatein bata di thi ki how we used to kiss each other on stairs and everything",
            dateHint = "Secret #22"
        ),
        SecretItem(
            id = 23,
            title = "The Deep Conversation",
            secretContent = "Jab tune mereko apne papa waali baat batayi thi na ki agar papa na hote toh Masturb. karleti, i felt so good on that day because relationship only builds on these things",
            dateHint = "Secret #23"
        ),
        SecretItem(
            id = 24,
            title = "Googling Celebrity Crushes",
            secretContent = "yaad h jab tune mereko apna celebrity crush bataya tha na? on that day i started googling ~ most hot celebrities to have crush on, and maine koi bhi random uthai aur tereko boldia ki i like her",
            dateHint = "Secret #24"
        ),
        SecretItem(
            id = 25,
            title = "Emily's Account & The Chats",
            secretContent = "actually this is the most fiery one, starting of our relationship, jab tune yaad h bola nahi tha emily ko ki gaurav chutiya h, uss time emily ne jab tereko account dia tha i actually opened it and vahi se chutiya vaali chats padhi thi",
            dateHint = "Secret #25"
        ),
        SecretItem(
            id = 26,
            title = "The Crypto Bluff",
            secretContent = "maine yaad h tereko bataya nahi tha ki mai crypto karta hu? mai bas voh fek raha tha aur mereko koi paise nahi mile the",
            dateHint = "Secret #26"
        )
    )

    // ------------------------------------------------------------------------
    // 5. SORRY CENTRE (DARK DEVIL / DEATH THEME) CONFIGURATION
    // ------------------------------------------------------------------------
    val SORRY_CENTRE_PASSWORD = "0802"
    val SORRY_COOLDOWN_MILLIS = 2L * 24 * 60 * 60 * 1000 // 2 days in ms
    val SORRY_KEY_BYPASS_COST = 1 // 1 key can unlock during cooldown

    data class ApologyItem(
        val number: Int,
        val title: String,
        val message: String,
        val sincereCommitment: String
    )

    val APOLOGIES_LIST = listOf(
        ApologyItem(
            number = 1,
            title = "Sorry for being a hurtful boyfriend",
            message = "Sorry for being a hurtful boyfriend, But it doesn't makes me unlovable towars you",
            sincereCommitment = "I never mean to hurt you. My commitment is to always treat you with warmth, patience, and love, proving that my heart is unconditionally yours."
        ),
        ApologyItem(
            number = 2,
            title = "Sorry for being flirtatious with aditi",
            message = "Sorry for being flirtatious with aditi, Ik its my mistake and I won't talk to her",
            sincereCommitment = "Ik its my mistake and I won't talk to her. My commitment is 100% loyalty, transparent honesty, and cutting off anyone that causes you pain."
        ),
        ApologyItem(
            number = 3,
            title = "Sorry for not having a man type body",
            message = "Sorry for not having a man type body that you wanted, I will go to gym and give you the product",
            sincereCommitment = "I will go to gym consistently, work out, build the physique you desire, and give you the product."
        ),
        ApologyItem(
            number = 4,
            title = "Sorry for not reaching out at your lowest",
            message = "Sorry for not reaching you out when you were at your lowest",
            sincereCommitment = "I am deeply sorry for not being there when you needed me most. My commitment is to always reach out proactively, comfort you, and never let you feel alone."
        ),
        ApologyItem(
            number = 5,
            title = "Sorry for not drinking pp milk",
            message = "Sorry for not drinking pp milk",
            sincereCommitment = "I will never refuse what you lovingly ask, take the best care of you, and do whatever brings that radiant smile back okay PP monster?"
        )
    )

    // ------------------------------------------------------------------------
    // 6. PROMISE CENTRE CONFIGURATION (UNTIL MARRIAGE)
    // ------------------------------------------------------------------------
    data class PromiseItem(
        val id: Int,
        val title: String,
        val promiseText: String,
        val iconEmoji: String
    )

    val MARRIAGE_PROMISES_HEADER = "Promises we will do & keep forever ❤️"

    val PROMISES_LIST = listOf(
        PromiseItem(
            id = 1,
            title = "We Will Never Breakup",
            promiseText = "we will never ever breakup, how much bad the fight is, we promise that we wont ever breakup and lose this relationship ever again",
            iconEmoji = "💍"
        ),
        PromiseItem(
            id = 2,
            title = "Fight For Each Other & Passwords Safe",
            promiseText = "we can hurt each other( but very less) but if someone says something to each other(external) then we will fight for each other. also no changing of password okie???",
            iconEmoji = "🛡️"
        ),
        PromiseItem(
            id = 3,
            title = "Equal Importance, Private Love & Dudu",
            promiseText = "we will plan out dates together and will give equal importance to each other, and no telling of relationship to anyone else. also pls give gaurav dudu everyday",
            iconEmoji = "🍼"
        ),
        PromiseItem(
            id = 4,
            title = "Study Hard: AIIMS & IIT",
            promiseText = "We will study hard and fight all the hard things which will come together, you in aiims and i in iit okie?",
            iconEmoji = "🎓"
        ),
        PromiseItem(
            id = 5,
            title = "Finally Marry Each Other",
            promiseText = "We will finally marry each other?",
            iconEmoji = "👰‍♀️"
        )
    )

    val DAILY_PROMISE_BUTTON_TEXT = "I love you and I promise u"
    val DAILY_REMINDER_NOTIFICATION_TEXT = "Hey ❤️ You haven't made today's promise yet."

    // ------------------------------------------------------------------------
    // 7. OUR ALBUM MEMORIES (PHOTO ALBUM LANE)
    // ------------------------------------------------------------------------
    data class AlbumPhoto(
        val id: Int,
        val pageUrl: String,
        val directUrl: String,
        val caption: String = ""
    )

    val ALBUM_PHOTOS = listOf(
        AlbumPhoto(1, "https://ibb.co/39vd8CKZ", "https://i.ibb.co/NdrCkyRB/20260903-125111.jpg", "Moments With You #1"),
        AlbumPhoto(2, "https://ibb.co/PGQyXHXn", "https://i.ibb.co/F4V1ZTZM/20260903-094941.jpg", "Moments With You #2"),
        AlbumPhoto(3, "https://ibb.co/RTq8x11C", "https://i.ibb.co/Zz09n33h/Snapchat-745221165.jpg", "Moments With You #3"),
        AlbumPhoto(4, "https://ibb.co/JwBm3myx", "https://i.ibb.co/fz1qDqn8/Snapchat-1135015821.jpg", "Moments With You #4"),
        AlbumPhoto(5, "https://ibb.co/mVPYwFy2", "https://i.ibb.co/Q3xqRjHT/Snapchat-11527268.jpg", "Moments With You #5"),
        AlbumPhoto(6, "https://ibb.co/8LFvsPKH", "https://i.ibb.co/DH3cWwVZ/20260903-094849.jpg", "Moments With You #6"),
        AlbumPhoto(7, "https://ibb.co/NntpNLzn", "https://i.ibb.co/Csmv9nFs/20260702-130344.jpg", "Moments With You #7"),
        AlbumPhoto(8, "https://ibb.co/hxWV3xSC", "https://i.ibb.co/FkwzRkFn/20260302-085230.jpg", "Moments With You #8"),
        AlbumPhoto(9, "https://ibb.co/0VRzVMzT", "https://i.ibb.co/DgHjgCjh/Snapchat-163226228.jpg", "Moments With You #9"),
        AlbumPhoto(10, "https://ibb.co/6JWXp4H9", "https://i.ibb.co/tPJLNbqj/20260903-125027.jpg", "Moments With You #10"),
        AlbumPhoto(11, "https://ibb.co/HfwDNLgT", "https://i.ibb.co/Xr0ZyfJx/Snapchat-2083747537.jpg", "Moments With You #11"),
        AlbumPhoto(12, "https://ibb.co/YFTKQV7Y", "https://i.ibb.co/ks6rBPgk/20260903-112828.jpg", "Moments With You #12"),
        AlbumPhoto(13, "https://ibb.co/39LMrtkC", "https://i.ibb.co/7d6RY7b1/20260227-090600.jpg", "Moments With You #13"),
        AlbumPhoto(14, "https://ibb.co/VYBkrGs6", "https://i.ibb.co/m5zP1K3p/20260702-114842.jpg", "Moments With You #14"),
        AlbumPhoto(15, "https://ibb.co/CTmcxgb", "https://i.ibb.co/VPJ1XRg/20260702-130414.jpg", "Moments With You #15"),
        AlbumPhoto(16, "https://ibb.co/KxMJmV7J", "https://i.ibb.co/m5L3Sht3/20260903-094932.jpg", "Moments With You #16"),
        AlbumPhoto(17, "https://ibb.co/k2Sj0wF3", "https://i.ibb.co/whzVCP1J/20260903-132123.jpg", "Moments With You #17"),
        AlbumPhoto(18, "https://ibb.co/qvn20P3", "https://i.ibb.co/L3PsQ2X/Snapchat-484244696.jpg", "Moments With You #18"),
        AlbumPhoto(19, "https://ibb.co/PZBhjndz", "https://i.ibb.co/BV8Z3x9K/20260227-091020.jpg", "Moments With You #19"),
        AlbumPhoto(20, "https://ibb.co/6ccpwNfq", "https://i.ibb.co/Y44KQbqV/20260903-095837.jpg", "Moments With You #20"),
        AlbumPhoto(21, "https://ibb.co/Wj8Rtw3", "https://i.ibb.co/CDCS2Zt/20260702-143437.jpg", "Moments With You #21"),
        AlbumPhoto(22, "https://ibb.co/G4Q775tP", "https://i.ibb.co/Nd2xxymY/Snapchat-1468373845.jpg", "Moments With You #22"),
        AlbumPhoto(23, "https://ibb.co/BKSB27pp", "https://i.ibb.co/fGPHdKgg/20260702-143644.jpg", "Moments With You #23"),
        AlbumPhoto(24, "https://ibb.co/ym2HtvQc", "https://i.ibb.co/fVJjf6xz/20260903-112838.jpg", "Moments With You #24"),
        AlbumPhoto(25, "https://ibb.co/bR5xP946", "https://i.ibb.co/84gJ0Zym/20260702-143325.jpg", "Moments With You #25"),
        AlbumPhoto(26, "https://ibb.co/8nQd0BLR", "https://i.ibb.co/XfG3b4rd/20260702-143406.jpg", "Moments With You #26"),
        AlbumPhoto(27, "https://ibb.co/WvGFZS3b", "https://i.ibb.co/pjbZcCPt/20260227-130255.jpg", "Moments With You #27"),
        AlbumPhoto(28, "https://ibb.co/xqY4X4xL", "https://i.ibb.co/fd9cqcjF/20260903-095119.jpg", "Moments With You #28"),
        AlbumPhoto(29, "https://ibb.co/YBGJm21X", "https://i.ibb.co/pjpNc2MQ/20260702-133931.jpg", "Moments With You #29"),
        AlbumPhoto(30, "https://ibb.co/VYH8919h", "https://i.ibb.co/whJ1gkgb/482567496-645840167845943-928555153382168944-n.jpg", "Moments With You #30"),
        AlbumPhoto(31, "https://ibb.co/rKG89YTZ", "https://i.ibb.co/V0Wn6Rrv/598713465-1226363002723046-5082453911285093126-n.png", "Moments With You #31"),
        AlbumPhoto(32, "https://ibb.co/v6stJfdv", "https://i.ibb.co/LXQGCBgh/Screenshot-2026-09-01-171742.png", "Moments With You #32"),
        AlbumPhoto(33, "https://ibb.co/wF5TwLq7", "https://i.ibb.co/FLR90W1X/image.png", "Moments With You #33"),
        AlbumPhoto(34, "https://ibb.co/QFG4m98X", "https://i.ibb.co/kgFtcK3M/551390659-1813262205985776-6592611384900370510-n.png", "Moments With You #34"),
        AlbumPhoto(35, "https://ibb.co/Kps4sk1V", "https://i.ibb.co/DHMcMyqw/1221784946470718.png", "Moments With You #35"),
        AlbumPhoto(36, "https://ibb.co/vCLxB3FH", "https://i.ibb.co/HLGD2tSV/1077238467722515.jpg", "Moments With You #36"),
        AlbumPhoto(37, "https://ibb.co/3y7zhZcg", "https://i.ibb.co/SXPrvCBF/2135680977192607.png", "Moments With You #37"),
        AlbumPhoto(38, "https://ibb.co/Cg0NX74", "https://i.ibb.co/sn3kDwc/1572925734034858.png", "Moments With You #38"),
        AlbumPhoto(39, "https://ibb.co/0Rf6Ltrt", "https://i.ibb.co/dwQVvgDg/23878194941844506.png", "Moments With You #39"),
        AlbumPhoto(40, "https://ibb.co/5XLTWk16", "https://i.ibb.co/wZ4yN7dR/2025-02-04-13-50-23.png", "Moments With You #40"),
        AlbumPhoto(41, "https://ibb.co/jPjTsW3t", "https://i.ibb.co/gLc3hPtY/2025-01-21-13-39-21.png", "Moments With You #41"),
        AlbumPhoto(42, "https://ibb.co/spwyJWY8", "https://i.ibb.co/rK64fdrD/Snapchat-198633773.jpg", "Moments With You #42"),
        AlbumPhoto(43, "https://ibb.co/V0W7vXdb", "https://i.ibb.co/PsZ75BRJ/Chat-GPT-Image-Jul-4-2026-09-34-51-PM.png", "Moments With You #43"),
        AlbumPhoto(44, "https://ibb.co/Zz44bVvM", "https://i.ibb.co/GQYYr0G5/Chat-GPT-Image-Jul-4-2026-08-19-20-PM.png", "Moments With You #44"),
        AlbumPhoto(45, "https://ibb.co/3txfZqg", "https://i.ibb.co/hpGXzj3/734958089-1553189412923012-6885745082288488615-n.jpg", "Moments With You #45"),
        AlbumPhoto(46, "https://ibb.co/b5X67gJW", "https://i.ibb.co/xq623SHC/20260227-130231-1.jpg", "Moments With You #46"),
        AlbumPhoto(47, "https://ibb.co/zWqFXt3j", "https://i.ibb.co/FkRX5dQN/20260702-142810.jpg", "Moments With You #47"),
        AlbumPhoto(48, "https://ibb.co/FLXYqCqH", "https://i.ibb.co/gLdWFhF4/2025-02-04-14-28-13.png", "Moments With You #48"),
        AlbumPhoto(49, "https://ibb.co/PvW9LbKC", "https://i.ibb.co/B56rp7bs/Capture.png", "Moments With You #49")
    )

    // ------------------------------------------------------------------------
    // 8. PROBLEM CENTRE (SECRET DIARY / SAMSUNG NOTES STYLE)
    // ------------------------------------------------------------------------
    val PROBLEM_CENTRE_HEADER_QUOTE = "Problems you don't wanna share to me? Here you go, its your personal journal"
    val MEDITATION_ABOUT_TEXT = "Sorry if i made you mad, here come relax"

    // ------------------------------------------------------------------------
    // 9. BOTTOM FOOTER / CONTACT SECTION CONFIGURATION
    // ------------------------------------------------------------------------
    object ContactConfig {
        val MY_PHONE_NUMBER = "+91 7696664080" // Indian direct line
        val INSTAGRAM_HANDLE = "@itsgaurav_0208"
        val INSTAGRAM_URL = "https://www.instagram.com/itsgaurav_0208/"

        val CALL_ME_TEXT = "Wanna call me? — $MY_PHONE_NUMBER"
        val TEXT_ME_TEXT = "Wanna text me? Open Instagram and I am right there on top"
        val REST_TEXT = "Wanna do nothing, just rest? Still call me and say nothing ❤️"
    }

    // ------------------------------------------------------------------------
    // 11. KEYS REWARD SYSTEM DEFAULTS
    // ------------------------------------------------------------------------
    val INITIAL_KEYS = 3 // Starting bonus keys so she can test the forgiveness mechanics!
}
