package ge.nlatsabidze.walletfluent.ui.personalInfo.UserProfile.UserProfileRepository

interface UserProfileRepository {
    fun initializeFirebase()
    fun setInformationFromDatabase()
}