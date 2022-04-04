package ge.nlatsabidze.walletfluent.ui.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.ui.entry.entryRepository.FirebaseUserRepositoryImpl
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginRegisterViewModel @Inject constructor(private val firebaseRepository: FirebaseUserRepositoryImpl) :
    ViewModel() {

    private var _userMutableLive = MutableSharedFlow<Boolean>()
    val userMutableLiveFlow: MutableSharedFlow<Boolean> get() = _userMutableLive

    private var _dialogError = MutableStateFlow("")
    val dialogError: MutableStateFlow<String> get() = _dialogError

    init {
        _dialogError = firebaseRepository.getDialogErrorValue()
    }

    fun register(email: String, password: String, name: String, balance: Int) {
        viewModelScope.launch {
            firebaseRepository.register(email, password, name, balance).collect {
                _userMutableLive.emit(it)
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            firebaseRepository.login(email, password).collect {
                _userMutableLive.emit(it)
            }
        }
    }

    fun resetPassword(email: String) {
        firebaseRepository.resetUserPassword(email)
    }

    fun changeRepositoryValue() {
        firebaseRepository.changeRepositoryDialogError()
    }

}




