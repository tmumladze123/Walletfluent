package ge.nlatsabidze.walletfluent.ui.personalInfo.UserProfile.UserProfileRepository

import ge.nlatsabidze.walletfluent.Resource
import ge.nlatsabidze.walletfluent.ui.entry.userData.UserState
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {
    fun initializeFirebase()
    fun setInformationFromDatabase(): Flow<Resource<UserState>>
}