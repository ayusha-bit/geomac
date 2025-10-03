package io.silentsea.geomac.core

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map
import ru.astrainteractive.klibs.kstorage.suspend.FlowMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultFlowMutableKrate

class Prefs(
    private val prefsStore: DataStore<Preferences>,
) {
    val isPermissionRevoked: FlowMutableKrate<Boolean> = DefaultFlowMutableKrate(
        factory = { false },
        loader = {
            prefsStore.data.map { it[booleanPreferencesKey(IS_PERMISSION_REVOKED)] }
        },
        saver = { value ->
            prefsStore.edit {
                it[booleanPreferencesKey(IS_PERMISSION_REVOKED)] = value
            }
        }
    )

    companion object {
        private const val IS_PERMISSION_REVOKED = "is_permission_revoked"
    }
}