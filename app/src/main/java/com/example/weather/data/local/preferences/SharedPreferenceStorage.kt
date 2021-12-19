package com.example.weather.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface PreferenceStorage {
    var timeLoadedAt: Long
    var isFirstRun: Boolean
}

@Singleton
class SharedPreferenceStorage @Inject constructor(context: Context) : PreferenceStorage {
    private val preferences: Lazy<SharedPreferences> = lazy {
        context.applicationContext.getSharedPreferences("WEATHER_PREFERENCES", Context.MODE_PRIVATE)
    }

    override var timeLoadedAt by LongPreferences(preferences, "LAST_LOAD_TIME", 0)
    override var isFirstRun by BooleanPreferences(preferences, "IS_FIRST_RUN", true)
}

class LongPreferences(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Long
) : ReadWriteProperty<Any, Long> {
    override fun getValue(thisRef: Any, property: KProperty<*>): Long {
        return preferences.value.getLong(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Long) {
        preferences.value.edit { putLong(name, value) }
    }
}

class BooleanPreferences(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Boolean
) : ReadWriteProperty<Any, Boolean> {
    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
        return preferences.value.getBoolean(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        preferences.value.edit { putBoolean(name, value) }
    }
}