package ge.nlatsabidze.walletfluent.ui.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.Resource
import ge.nlatsabidze.walletfluent.ui.entry.entryRepository.FirebaseUserRepositoryImpl
import ge.nlatsabidze.walletfluent.util.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginRegisterViewModel @Inject constructor(
    private val firebaseRepository: FirebaseUserRepositoryImpl,
    private val dispatchers: Dispatchers
) :
    ViewModel() {

    private var _userMutableLive = MutableSharedFlow<Resource<AuthResult>>()
    val userMutableLiveFlow: MutableSharedFlow<Resource<AuthResult>> get() = _userMutableLive

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
        dispatchers.launchBackground(viewModelScope) {
            firebaseRepository.login(email, password).collect {
                _userMutableLive.emit(it)
            }
        }
    }

    fun validate(email: String, password: String): Boolean =
        email.isNotEmpty() && password.isNotEmpty()

    fun validateRegister(email: String, password: String, name: String): Boolean =
        email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()

    fun resetPassword(email: String) = firebaseRepository.resetUserPassword(email)

    fun changeRepositoryValue() = firebaseRepository.changeRepositoryDialogError()

}





