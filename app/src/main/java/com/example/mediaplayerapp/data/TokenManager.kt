package com.example.mediaplayerapp.data

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveToken(token: String, createdAt: Long = System.currentTimeMillis()) {
        prefs.edit()
            .putString(KEY_TOKEN, token)
            .putLong(KEY_TIMESTAMP, createdAt)
            .apply()
    }

    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    fun getTokenTimestamp(): Long {
        return prefs.getLong(KEY_TIMESTAMP, 0L)
    }

    fun isTokenValid(currentTime: Long = System.currentTimeMillis()): Boolean {
        val token = getToken()
        if (token.isNullOrEmpty()) return false
        val timestamp = getTokenTimestamp()
        val diff = currentTime - timestamp
        return diff >= 0 && diff < TOKEN_VALIDITY_DURATION_MS
    }

    fun clearToken() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val PREF_NAME = "auth_prefs"
        private const val KEY_TOKEN = "access_token"
        private const val KEY_TIMESTAMP = "token_timestamp"
        const val TOKEN_VALIDITY_DURATION_MS = 10 * 60 * 1000L // 10 minutes
    }
}