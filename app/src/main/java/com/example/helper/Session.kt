package com.example.helper

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//class Session private constructor(private val dataStore: DataStore<Preferences>){
//    private val SESSION_KEY = booleanPreferencesKey("session")
//    private val TOKEN_KEY = stringPreferencesKey("token")
//
//    fun getSession(): Flow<Boolean> {
//        return dataStore.data.map { preferences ->
//            preferences[SESSION_KEY] ?: false
//        }
//    }
//
//    suspend fun saveSession(isLoggedIn: Boolean){
//        dataStore.edit { preferences ->
//            preferences[SESSION_KEY] = isLoggedIn
//        }
//    }
//
//    fun getToken(): Flow<String> {
//        return dataStore.data.map { preferences ->
//            preferences[TOKEN_KEY] ?: ""
//        }
//    }
//
//    suspend fun saveToken(token: String){
//        dataStore.edit { preferences ->
//            preferences[TOKEN_KEY] = token
//
//        }
//    }
//
//    companion object {
//        @Volatile
//        private var INSTANCE: Session? = null
//
//        fun getInstance(dataStore: DataStore<Preferences>): Session {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Session(dataStore)
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}

class Session(context: Context) {
    companion object {
        const val PREFS_NAME = "user_pref"
        const val TOKEN = "token"
        const val LOGGED_IN = "logged_in"
    }

    private var preferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveToken(token: String){
        val editor = preferences.edit()
        editor.putString(TOKEN, token)
        editor.apply()
    }

    fun deleteToken(){
        val editor = preferences.edit()
        editor.clear()
        editor.apply()
    }

    fun saveLogin(value: Boolean){
        val editor = preferences.edit()
        if (value){
            editor.putBoolean(LOGGED_IN, value)
            editor.apply()
        } else {
            editor.clear()
            editor.apply()
        }
    }

    fun getLogin(): Boolean {
        return preferences.getBoolean(LOGGED_IN, false)
    }

    fun getToken(): String? {
        return preferences.getString(TOKEN, "")
    }
}