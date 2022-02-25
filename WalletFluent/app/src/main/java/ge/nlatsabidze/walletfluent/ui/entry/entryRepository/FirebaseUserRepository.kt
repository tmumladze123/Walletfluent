package ge.nlatsabidze.walletfluent.ui.entry.entryRepository

interface FirebaseUserRepository {
    fun register(email: String?, password: String?, name: String, balance: Int)
    fun login(email: String?, password: String?)
    fun resetUserPassword(email: String?)
}