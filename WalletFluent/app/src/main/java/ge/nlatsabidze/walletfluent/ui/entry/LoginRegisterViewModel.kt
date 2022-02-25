package ge.nlatsabidze.walletfluent.ui.entry

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.ui.entry.entryRepository.FirebaseUserRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginRegisterViewModel @Inject constructor(private val firebaseRepository: FirebaseUserRepositoryImpl) :
    ViewModel() {

    private var _userMutableLive = MutableStateFlow<Boolean>(false)
    val userMutableLiveFlow: MutableStateFlow<Boolean> get() = _userMutableLive

    private var _dialogError = MutableStateFlow("")
    val dialogError: MutableStateFlow<String> get() = _dialogError

    init {
        _userMutableLive = firebaseRepository.getRegisteredValue()
        _dialogError = firebaseRepository.getDialogErrorValue()
    }

    fun register(email: String, password: String, name: String, balance: Int) {
        firebaseRepository.register(email, password, name, balance)
    }

    fun login(email: String, password: String) {
        firebaseRepository.login(email, password)
    }

    fun resetPassword(email: String) {
        firebaseRepository.resetUserPassword(email)
    }

    fun changeUserValue() {
        firebaseRepository.changeUserFlowValue()
    }

    fun changeRepositoryValue() {
        firebaseRepository.changeRepositoryDialogError()
    }

}




