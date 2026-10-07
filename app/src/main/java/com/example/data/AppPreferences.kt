package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.config.AppConfig
import java.util.Calendar

class AppPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("our_little_world_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_KEYS_COUNT = "key_keys_count"
        private const val KEY_FLAPPY_HIGH_SCORE = "key_flappy_high_score"
        private const val KEY_CATCH_HIGH_SCORE = "key_catch_high_score"
        
        private const val KEY_CURRENT_STREAK = "key_current_streak"
        private const val KEY_BEST_STREAK = "key_best_streak"
        private const val KEY_LAST_PROMISE_TIME = "key_last_promise_time"
        
        private const val KEY_LAST_SECRET_UNLOCK_TIME = "key_last_secret_unlock_time"
        private const val KEY_UNLOCKED_SECRET_IDS = "key_unlocked_secret_ids"

        private const val KEY_LAST_SORRY_UNLOCK_TIME = "key_last_sorry_unlock_time"
        private const val KEY_UNLOCKED_SORRY_IDS = "key_unlocked_sorry_ids"
        
        private const val KEY_SELECTED_MOOD = "key_selected_mood"
        private const val KEY_LAST_MOOD_TIME = "key_last_mood_time"

        private const val KEY_INITIALIZED = "key_prefs_initialized"
    }

    init {
        if (!prefs.getBoolean(KEY_INITIALIZED, false)) {
            prefs.edit()
                .putInt(KEY_KEYS_COUNT, AppConfig.INITIAL_KEYS)
                .putBoolean(KEY_INITIALIZED, true)
                .apply()
        }
    }

    // --- KEYS REWARD SYSTEM ---
    fun getKeysCount(): Int = prefs.getInt(KEY_KEYS_COUNT, AppConfig.INITIAL_KEYS)

    fun addKeys(amount: Int): Int {
        val newCount = (getKeysCount() + amount).coerceAtLeast(0)
        prefs.edit().putInt(KEY_KEYS_COUNT, newCount).apply()
        return newCount
    }

    fun spendKeys(amount: Int): Boolean {
        val current = getKeysCount()
        if (current >= amount) {
            prefs.edit().putInt(KEY_KEYS_COUNT, current - amount).apply()
            return true
        }
        return false
    }

    // --- HIGH SCORES ---
    fun getFlappyHighScore(): Int = prefs.getInt(KEY_FLAPPY_HIGH_SCORE, 0)

    fun saveFlappyScore(score: Int): Boolean {
        val high = getFlappyHighScore()
        if (score > high) {
            prefs.edit().putInt(KEY_FLAPPY_HIGH_SCORE, score).apply()
            return true
        }
        return false
    }

    fun getCatchFacesHighScore(): Int = prefs.getInt(KEY_CATCH_HIGH_SCORE, 0)

    fun saveCatchFacesScore(score: Int): Boolean {
        val high = getCatchFacesHighScore()
        if (score > high) {
            prefs.edit().putInt(KEY_CATCH_HIGH_SCORE, score).apply()
            return true
        }
        return false
    }

    // --- PROMISE DAILY STREAK ---
    fun getCurrentStreak(): Int {
        checkStreakIntegrity()
        return prefs.getInt(KEY_CURRENT_STREAK, 0)
    }

    fun getBestStreak(): Int = prefs.getInt(KEY_BEST_STREAK, 0)

    fun getLastPromiseTime(): Long = prefs.getLong(KEY_LAST_PROMISE_TIME, 0L)

    fun isPromiseMadeToday(): Boolean {
        val lastTime = getLastPromiseTime()
        if (lastTime == 0L) return false
        return isSameDay(lastTime, System.currentTimeMillis())
    }

    /**
     * Record the daily promise button press.
     * Returns a pair of (newStreak, wasAlreadyPressedToday).
     */
    fun recordPromisePress(): Pair<Int, Boolean> {
        val now = System.currentTimeMillis()
        val lastTime = getLastPromiseTime()
        val currentStreak = prefs.getInt(KEY_CURRENT_STREAK, 0)
        val bestStreak = prefs.getInt(KEY_BEST_STREAK, 0)

        if (lastTime > 0L && isSameDay(lastTime, now)) {
            // Already pressed today
            return Pair(currentStreak, true)
        }

        val newStreak: Int
        if (lastTime > 0L && isConsecutiveDay(lastTime, now)) {
            newStreak = currentStreak + 1
        } else {
            // Missed day or first time
            newStreak = 1
        }

        val newBest = maxOf(newStreak, bestStreak)
        prefs.edit()
            .putInt(KEY_CURRENT_STREAK, newStreak)
            .putInt(KEY_BEST_STREAK, newBest)
            .putLong(KEY_LAST_PROMISE_TIME, now)
            .apply()

        return Pair(newStreak, false)
    }

    private fun checkStreakIntegrity() {
        val lastTime = getLastPromiseTime()
        if (lastTime == 0L) return
        val now = System.currentTimeMillis()
        if (!isSameDay(lastTime, now) && !isConsecutiveDay(lastTime, now)) {
            // More than 1 day elapsed, streak is broken
            prefs.edit().putInt(KEY_CURRENT_STREAK, 0).apply()
        }
    }

    private fun isSameDay(time1: Long, time2: Long): Boolean {
        val cal1 = Calendar.getInstance().apply { timeInMillis = time1 }
        val cal2 = Calendar.getInstance().apply { timeInMillis = time2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    private fun isConsecutiveDay(prevTime: Long, currentTime: Long): Boolean {
        val calPrev = Calendar.getInstance().apply { timeInMillis = prevTime }
        val calCurrent = Calendar.getInstance().apply { timeInMillis = currentTime }
        calPrev.add(Calendar.DAY_OF_YEAR, 1)
        return calPrev.get(Calendar.YEAR) == calCurrent.get(Calendar.YEAR) &&
               calPrev.get(Calendar.DAY_OF_YEAR) == calCurrent.get(Calendar.DAY_OF_YEAR)
    }

    // --- SECRET CENTRE COOLDOWN & UNLOCKS ---
    fun getLastSecretUnlockTime(): Long = prefs.getLong(KEY_LAST_SECRET_UNLOCK_TIME, 0L)

    fun getSecretCooldownRemainingMillis(): Long {
        val lastUnlock = getLastSecretUnlockTime()
        if (lastUnlock == 0L) return 0L
        val elapsed = System.currentTimeMillis() - lastUnlock
        val remaining = AppConfig.SECRET_WEEKLY_COOLDOWN_MILLIS - elapsed
        return if (remaining > 0) remaining else 0L
    }

    fun isSecretWeeklyCooldownActive(): Boolean = getSecretCooldownRemainingMillis() > 0L

    fun getUnlockedSecretIds(): Set<Int> {
        val raw = prefs.getStringSet(KEY_UNLOCKED_SECRET_IDS, emptySet()) ?: emptySet()
        return raw.mapNotNull { it.toIntOrNull() }.toSet()
    }

    fun unlockSecret(id: Int, bypassCooldown: Boolean = false): Boolean {
        val now = System.currentTimeMillis()
        val unlocked = getUnlockedSecretIds().toMutableSet()
        unlocked.add(id)

        val edit = prefs.edit()
        edit.putStringSet(KEY_UNLOCKED_SECRET_IDS, unlocked.map { it.toString() }.toSet())
        if (!bypassCooldown) {
            edit.putLong(KEY_LAST_SECRET_UNLOCK_TIME, now)
        }
        edit.apply()
        return true
    }

    fun resetSecretCooldownWithKey(): Boolean {
        if (spendKeys(AppConfig.SECRET_KEY_BYPASS_COST)) {
            prefs.edit().putLong(KEY_LAST_SECRET_UNLOCK_TIME, 0L).apply()
            return true
        }
        return false
    }

    // --- SORRY CENTRE COOLDOWN & UNLOCKS ---
    fun getLastSorryUnlockTime(): Long = prefs.getLong(KEY_LAST_SORRY_UNLOCK_TIME, 0L)

    fun getSorryCooldownRemainingMillis(): Long {
        val lastUnlock = getLastSorryUnlockTime()
        if (lastUnlock == 0L) return 0L
        val elapsed = System.currentTimeMillis() - lastUnlock
        val remaining = AppConfig.SORRY_COOLDOWN_MILLIS - elapsed
        return if (remaining > 0) remaining else 0L
    }

    fun isSorryCooldownActive(): Boolean = getSorryCooldownRemainingMillis() > 0L

    fun getUnlockedSorryIds(): Set<Int> {
        val raw = prefs.getStringSet(KEY_UNLOCKED_SORRY_IDS, emptySet()) ?: emptySet()
        return raw.mapNotNull { it.toIntOrNull() }.toSet()
    }

    fun unlockApology(number: Int, bypassCooldown: Boolean = false): Boolean {
        val now = System.currentTimeMillis()
        val unlocked = getUnlockedSorryIds().toMutableSet()
        unlocked.add(number)

        val edit = prefs.edit()
        edit.putStringSet(KEY_UNLOCKED_SORRY_IDS, unlocked.map { it.toString() }.toSet())
        if (!bypassCooldown) {
            edit.putLong(KEY_LAST_SORRY_UNLOCK_TIME, now)
        }
        edit.apply()
        return true
    }

    fun resetSorryCooldownWithKey(): Boolean {
        if (spendKeys(AppConfig.SORRY_KEY_BYPASS_COST)) {
            prefs.edit().putLong(KEY_LAST_SORRY_UNLOCK_TIME, 0L).apply()
            return true
        }
        return false
    }

    // --- DAILY MOOD CHECK-IN ---
    fun getSelectedMood(): String = prefs.getString(KEY_SELECTED_MOOD, "CHAD") ?: "CHAD"

    fun getLastMoodTime(): Long = prefs.getLong(KEY_LAST_MOOD_TIME, 0L)

    fun isMoodCheckedInToday(): Boolean {
        val lastTime = getLastMoodTime()
        if (lastTime == 0L) return false
        return isSameDay(lastTime, System.currentTimeMillis())
    }

    fun saveMood(moodId: String) {
        prefs.edit()
            .putString(KEY_SELECTED_MOOD, moodId)
            .putLong(KEY_LAST_MOOD_TIME, System.currentTimeMillis())
            .apply()
    }
}
