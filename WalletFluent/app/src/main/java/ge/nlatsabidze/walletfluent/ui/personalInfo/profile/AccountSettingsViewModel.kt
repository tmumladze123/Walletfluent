package ge.nlatsabidze.walletfluent.ui.personalInfo.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.checkConnectivity.CheckInternetConnection
import ge.nlatsabidze.walletfluent.ui.entry.entryRepository.FirebaseUserRepository
import ge.nlatsabidze.walletfluent.ui.personalInfo.profile.profileRepository.AccountsRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSettingsViewModel @Inject constructor(
    var firebaseRepository: FirebaseUserRepository,
    var profileRepository: AccountsRepository,
    application: Application,
    var checkInternetConnection: CheckInternetConnection
) : AndroidViewModel(application) {

    private val _userName = MutableStateFlow<String>("")
    val userName: MutableStateFlow<String> get() = _userName

    private val _showChangePasswordDialog = MutableSharedFlow<String>()
    val showChangePasswordDialog: MutableSharedFlow<String> get() = _showChangePasswordDialog

    fun initializeFirebase() {
        profileRepository.initializeFirebaseRepo()
    }

    fun changeUserPassword() {
        profileRepository.UserDatabase().get().addOnCompleteListener {
            if (it.result?.exists() == true) {
                val email = it.result!!.child("email").value.toString()
                firebaseRepository.resetUserPassword(email)
                viewModelScope.launch {
                    _showChangePasswordDialog.emit("Change password")
                }
            }
        }
    }

    fun getUserName() {
        profileRepository.UserDatabase().get().addOnCompleteListener {
            if (it.result?.exists() == true) {
                val userName = it.result!!.child("name").value.toString()
                viewModelScope.launch {
                    _userName.value = userName
                }
            }
        }
    }

    fun checkConnection(): Boolean {
        return checkInternetConnection.isOnline(getApplication())
    }

    fun logOutCurrentUser() {
        firebaseRepository.logOutUser()
    }

}