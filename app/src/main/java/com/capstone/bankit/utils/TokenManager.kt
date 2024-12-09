package com.capstone.bankit.utils

import android.content.Context
import android.content.SharedPreferences

class TokenManager private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveToken(token: String?) {
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
    }

    val token: String?
        get() = sharedPreferences.getString(TOKEN_KEY, null)

    fun clearToken() {
        sharedPreferences.edit().remove(TOKEN_KEY).apply()
    }

    companion object {
        private const val PREFS_NAME = "AppPreferences"
        private const val TOKEN_KEY = "auth_token"
        private var instance: TokenManager? = null
        @Synchronized
        fun getInstance(context: Context): TokenManager? {
            if (instance == null) {
                instance = TokenManager(context)
            }
            return instance
        }
    }
}