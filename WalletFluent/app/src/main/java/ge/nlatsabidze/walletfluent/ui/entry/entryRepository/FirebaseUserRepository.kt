package ge.nlatsabidze.walletfluent.ui.entry.entryRepository

import kotlinx.coroutines.flow.Flow

interface FirebaseUserRepository {
    fun register(email: String?, password: String?, name: String, balance: Int): Flow<Boolean>
    fun login(email: String?, password: String?): Flow<Boolean>
    fun resetUserPassword(email: String?)
}