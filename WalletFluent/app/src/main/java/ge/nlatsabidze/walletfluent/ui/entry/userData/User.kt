package ge.nlatsabidze.walletfluent.ui.entry.userData

data class User(val uniqueId: String? = null,
                val email: String? = null,
                val name: String? = null,
                val password: String? = null,
                val balance: Int? = null)
