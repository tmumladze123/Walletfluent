package ge.nlatsabidze.walletfluent.ui.entry.entryRepository

import com.google.firebase.auth.AuthResult
import ge.nlatsabidze.walletfluent.Resource
import kotlinx.coroutines.flow.Flow

interface FirebaseUserRepository {
    suspend fun register(email: String?, password: String?, name: String, balance: Int): Flow<Resource<AuthResult>>
    suspend fun login(email: String?, password: String?): Flow<Resource<AuthResult>>
    fun resetUserPassword(email: String?)
}