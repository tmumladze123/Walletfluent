package ge.nlatsabidze.walletfluent

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

enum class UiMode {
    LIGHT, DARK
}

class SettingsManager @Inject constructor(context: Context) {

    companion object {
        val IS_DARK_MODE = booleanPreferencesKey("dark_mode")
        private val Context.dataStore by preferencesDataStore("settings")
    }

    private val mDataStore: DataStore<Preferences> = context.dataStore

    val uiModeFlow: Flow<UiMode> = mDataStore.data
        .map {
            when(it[IS_DARK_MODE] ?: false) {
                true -> UiMode.DARK
                false -> UiMode.LIGHT
            }
        }

    suspend fun setUpUiMode(uiMode: UiMode) {
        mDataStore.edit {
            it[IS_DARK_MODE] = when(uiMode) {
                UiMode.LIGHT -> false
                UiMode.DARK -> true
            }
        }
    }
}