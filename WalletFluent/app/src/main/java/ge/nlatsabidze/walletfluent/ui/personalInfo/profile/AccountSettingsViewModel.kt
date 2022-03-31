package ge.nlatsabidze.walletfluent.ui.personalInfo.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.ui.entry.entryRepository.FirebaseUserRepositoryImpl
import ge.nlatsabidze.walletfluent.ui.personalInfo.profile.profileRepository.AccountsRepositoryImpl
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSettingsViewModel @Inject constructor(
    var firebaseRepository: FirebaseUserRepositoryImpl,
    var profileRepository: AccountsRepositoryImpl
) : ViewModel() {

    private var _userName = MutableStateFlow<String>("")
    val userName: MutableStateFlow<String> get() = _userName

    private var _showChangePasswordDialog = MutableSharedFlow<String>()
    val showChangePasswordDialog: MutableSharedFlow<String> get() = _showChangePasswordDialog

    fun initializeFirebase() {
        profileRepository.initializeFirebase()
    }

    fun changeUserPassword() {
        viewModelScope.launch {
            profileRepository.changeUserPassword().collect { dialog ->
                _showChangePasswordDialog.emit(dialog.data.toString())
            }
        }
    }

    fun getUserName() {
        viewModelScope.launch {
            profileRepository.getUserName().collect { userName ->
                _userName.value = userName.data.toString()
            }
        }
    }

    fun logOutCurrentUser() {
        firebaseRepository.logOutUser()
    }

}