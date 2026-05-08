package mungarro.carlos.biometria_mungarrocarlos.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {

    private val Context.dataStore by preferencesDataStore("session_preferences")

    companion object {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val USERNAME = stringPreferencesKey("username")
        val BIOMETRICS_ACTIVE = booleanPreferencesKey("biometrics_active")
    }

    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data.map {
        it[IS_LOGGED_IN] ?: false
    }

    val usernameFlow: Flow<String> = context.dataStore.data.map {
        it[USERNAME] ?: ""
    }

    val biometricsFlow: Flow<Boolean> = context.dataStore.data.map {
        it[BIOMETRICS_ACTIVE] ?: false
    }

    suspend fun saveSession(username: String) {
        context.dataStore.edit {
            it[USERNAME] = username
            it[IS_LOGGED_IN] = true
        }
    }


    suspend fun activateBiometrics(active: Boolean) {
        context.dataStore.edit {
            it[BIOMETRICS_ACTIVE] = active
        }
    }

    suspend fun logout(biometricsActive: Boolean) {
        context.dataStore.edit {
            it[IS_LOGGED_IN] = false
            if (!biometricsActive) {
                it[USERNAME] = ""
            }
        }
    }

    suspend fun loginWithBiometrics() {
        context.dataStore.edit {
            it[IS_LOGGED_IN] = true
        }
    }
}