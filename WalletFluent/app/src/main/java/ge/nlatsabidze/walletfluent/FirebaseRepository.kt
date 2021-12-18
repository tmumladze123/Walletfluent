package ge.nlatsabidze.walletfluent

import android.app.Application
import android.util.Log.d
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


class FirebaseRepository @Inject constructor(private val application: Application) {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private var _currentUser = MutableStateFlow(false)
    val currentUser: MutableStateFlow<Boolean> get() = _currentUser

    private var _dialogError = MutableStateFlow(false)
    val dialogError: MutableStateFlow<Boolean> get() = _dialogError

    private var _verifyError = MutableStateFlow(false)
    val verifyError: MutableStateFlow<Boolean> get() = _verifyError

    private var _resetPasswordError = MutableStateFlow(false)
    val resetPasswordError: MutableStateFlow<Boolean> get() = _resetPasswordError

    private var _resetPasswordDialog = MutableStateFlow(false)
    val resetPasswordDialog: MutableStateFlow<Boolean> get() = _resetPasswordDialog

    fun register(email: String?, password: String?) {
        firebaseAuth.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //  _userMutableLiveFlow.value = true
                } else {

                }
            }
    }

    fun login(email: String?, password: String?) {
        d("esaaa :", password.toString() + "  " + email.toString())
        if (email!!.isNotEmpty() && password!!.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { Task ->
                if (Task.isSuccessful) {
                    val firebaseUser = firebaseAuth.currentUser
                    if (firebaseUser!!.isEmailVerified) {
                        _currentUser.value = true
                    } else {
                        firebaseUser.sendEmailVerification()
                        _verifyError.value = true
                    }
                } else {
                    _dialogError.value = true
                }
            }
        }
    }

    fun resetUserPassword(email: String?) {
        if (email!!.isNotEmpty()) {
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { userEmail ->
                    if (userEmail.isSuccessful) {
                        _resetPasswordError.value = true
                    }
                }
        } else {
            _resetPasswordDialog.value = true
        }
    }

    fun changeUserFlowValue() {
        _currentUser.value = false
    }

    fun changeDialogFlowValue() {
        _dialogError.value = false
    }

    fun changeVerifyFlowValue() {
        _verifyError.value = false
    }

    fun changeResetFlowValue() {
        _resetPasswordError.value = false
    }

    fun changeResetPasswordDialogValue() {
        _resetPasswordDialog.value = false
    }
}