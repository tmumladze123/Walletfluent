package ge.nlatsabidze.walletfluent.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository) :
    ViewModel() {

    private var _userMutableLive = MutableStateFlow<Boolean>(false)
    val userMutableLiveFlow: MutableStateFlow<Boolean> get() = _userMutableLive

    private var _showDialogError = MutableStateFlow(false)
    val showDialogError: MutableStateFlow<Boolean> get() = _showDialogError

    private var _showVerifyError = MutableStateFlow(false)
    val showVerifyError: MutableStateFlow<Boolean> get() = _showVerifyError

    private var _showResetPasswordError = MutableStateFlow(false)
    val showResetPasswordError: MutableStateFlow<Boolean> get() = _showResetPasswordError

    private var _showResetDialogError = MutableStateFlow(false)
    val showResetDialogError: MutableStateFlow<Boolean> get() = _showResetDialogError


    fun login(email: String, password: String) {

        firebaseRepository.login(email, password)

        viewModelScope.launch {
            firebaseRepository.currentUser.collectLatest {
                _userMutableLive.value = it
            }
        }

        viewModelScope.launch {
            firebaseRepository.verifyError.collectLatest {
                _showVerifyError.value = it
            }
        }

        viewModelScope.launch {
            firebaseRepository.dialogError.collectLatest {
                _showDialogError.value = it
            }
        }
    }

    fun resetPassword(email: String) {
        firebaseRepository.resetUserPassword(email)

        viewModelScope.launch {
            firebaseRepository.resetPasswordError.collectLatest {
                _showResetPasswordError.value = it
            }
        }

        viewModelScope.launch {
            firebaseRepository.resetPasswordDialog.collectLatest {
                _showResetPasswordError.value = it
            }
        }
    }

    fun changeUserValue() {
        firebaseRepository.changeUserFlowValue()
    }

    fun changeDialogValue() {
        firebaseRepository.changeDialogFlowValue()
    }

    fun changeVerifyValue() {
        firebaseRepository.changeVerifyFlowValue()
    }

    fun changeResetPasswordErrorValue() {
        firebaseRepository.changeResetFlowValue()
    }

    fun changeResetDialogError() {
        firebaseRepository.changeResetPasswordDialogValue()
    }
}




