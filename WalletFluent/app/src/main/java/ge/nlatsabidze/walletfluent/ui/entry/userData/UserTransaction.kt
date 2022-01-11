package ge.nlatsabidze.walletfluent.ui.entry.userData

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserTransaction(val amount: Int? = null,
                           val purpose: String? = null): Parcelable