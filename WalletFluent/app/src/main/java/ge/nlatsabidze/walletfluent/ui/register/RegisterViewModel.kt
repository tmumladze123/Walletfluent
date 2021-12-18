package ge.nlatsabidze.walletfluent.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.FirebaseRepository
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository) :ViewModel() {
    private var userMutableLiveData: MutableLiveData<FirebaseUser>? = null
    private var showDialogError: MutableLiveData<Boolean>? = null

    /*fun register(email: String, password: String) {
        appRepository.register(email, password)
    }

    fun getMutableLiveData(): MutableLiveData<FirebaseUser>? {
        userMutableLiveData = appRepository.getUserMutableLiveData()
        return userMutableLiveData
    }

    fun showDialogError(): MutableLiveData<Boolean>? {
        showDialogError = appRepository.getVerifyDialogError()
        return showDialogError
    }
*/

}