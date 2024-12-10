package com.capstone.bankit.utils

import android.content.Context
import android.content.SharedPreferences

class TokenManager private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveToken(token: String?, email: String?, name: String?) {
        sharedPreferences.edit().apply {
            putString(TOKEN_KEY, token)
            putString(EMAIL_KEY, email)
            putString(NAME_KEY, name)
            apply()
        }
    }

    val token: String?
        get() = sharedPreferences.getString(TOKEN_KEY, null)

    val email: String?
        get() = sharedPreferences.getString(EMAIL_KEY, null)

    val name: String?
        get() = sharedPreferences.getString(NAME_KEY, null)

    fun clearToken() {
        sharedPreferences.edit().apply {
            remove(TOKEN_KEY)
            remove(EMAIL_KEY)
            remove(NAME_KEY)
            apply()
        }
    }
    companion object {
        private const val PREFS_NAME = "AppPreferences"
        private const val TOKEN_KEY = "auth_token"
        private const val NAME_KEY = "auth_name"
        private const val EMAIL_KEY = "auth_email"

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