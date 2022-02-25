package ge.nlatsabidze.walletfluent.ui.personalInfo.profile.profileRepository

interface AccountsRepository {
    fun initializeFirebase()
    fun changeUserPassword()
    fun getUserName()
    fun logOutCurrentUser()
}