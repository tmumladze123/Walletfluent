package ge.nlatsabidze.walletfluent.ui.loginAndRegister

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginAndRegisterViewModel @Inject constructor(private val appRepository: FirebaseRepository) :ViewModel() {

    private var userMutableLiveData: MutableLiveData<FirebaseUser>? = null
    private var showDialogError: MutableLiveData<Boolean>? = null

    fun register(email: String, password: String) {
        appRepository.register(email, password)
    }

    fun login(email: String, password: String) {
        appRepository.login(email, password)
    }

    fun getMutableLiveData(): MutableLiveData<FirebaseUser>? {
        userMutableLiveData = appRepository.getUserMutableLiveData()
        return userMutableLiveData
    }

    fun showDialogError(): MutableLiveData<Boolean>? {
        showDialogError = appRepository.getDialogError()
        return showDialogError
    }

}