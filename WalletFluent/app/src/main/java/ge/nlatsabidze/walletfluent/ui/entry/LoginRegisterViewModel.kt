package ge.nlatsabidze.walletfluent.ui.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.FirebaseUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginRegisterViewModel @Inject constructor(private val firebaseRepository: FirebaseUserRepository) :
    ViewModel() {

    private var _userMutableLive = MutableStateFlow<Boolean>(false)
    val userMutableLiveFlow: MutableStateFlow<Boolean> get() = _userMutableLive

    private var _dialogError = MutableStateFlow("")
    val dialogError: MutableStateFlow<String> get() = _dialogError

    fun register(email: String, password: String, name: String, balance: Int) {
        firebaseRepository.register(email, password, name, balance)
        viewModelScope.launch {
            firebaseRepository.currentUser.collectLatest {
                _userMutableLive.value = it
            }
        }
        viewModelScope.launch {
            firebaseRepository.repositoryDialog.collectLatest {
                _dialogError.value = it
            }
        }

    }

    fun login(email: String, password: String) {
        firebaseRepository.login(email, password)
        viewModelScope.launch {
            firebaseRepository.currentUser.collectLatest {
                _userMutableLive.value = it
            }
        }
        viewModelScope.launch {
            firebaseRepository.repositoryDialog.collectLatest {
                _dialogError.value = it
            }
        }

    }

    fun resetPassword(email: String) {
        firebaseRepository.resetUserPassword(email)
        viewModelScope.launch {
            firebaseRepository.repositoryDialog.collectLatest {
                _dialogError.value = it
            }
        }
    }

    fun changeUserValue() {
        firebaseRepository.changeUserFlowValue()
    }

    fun changeRepositoryValue() {
        firebaseRepository.changeRepositoryDialogError()
    }

}




