package ge.nlatsabidze.walletfluent.ui.personalInfo.profile.profileRepository

import ge.nlatsabidze.walletfluent.Resource
import kotlinx.coroutines.flow.Flow

interface AccountsRepository {
    fun initializeFirebase()
    fun changeUserPassword(): Flow<Resource<String>>
    fun getUserName(): Flow<Resource<String>>
    fun logOutCurrentUser()
}