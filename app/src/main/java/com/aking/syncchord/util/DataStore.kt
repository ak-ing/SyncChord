package com.aking.syncchord.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.Preferences.Key
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

/**
 * Description:
 * Created by Rick at 2024-10-19 18:42.
 */

/**
 * 请勿在同一进程中为给定文件创建多个 DataStore 实例
 */
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

suspend fun <T> DataStore<Preferences>.set(key: Key<T>, value: T) {
    edit { preferences ->
        preferences[key] = value
    }
}

suspend fun <T> DataStore<Preferences>.get(key: Key<T>, default: T): T {
    val preferences = data.first()
    return preferences[key] ?: default
}

suspend fun <T> DataStore<Preferences>.get(key: Key<T>): T? {
    val preferences = data.first()
    return preferences[key]
}


/**
 * Returns true if this Preferences contains the specified key.
 *
 * @param key the key to check for
 */
suspend fun <T> DataStore<Preferences>.contains(key: Key<T>) = data.first().contains(key)

/**
 * Remove a preferences from this dataStore.
 *
 * @param key the key to remove this MutablePreferences
 * @return the original value of this preference key.
 */
suspend fun <T> DataStore<Preferences>.remove(key: Key<T>) {
    edit { preferences ->
        preferences.remove(key)
    }
}