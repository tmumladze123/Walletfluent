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

    private var _repositoryDialogError = MutableStateFlow("")
    val repositoryDialog: MutableStateFlow<String> get() = _repositoryDialogError

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
                        _repositoryDialogError.value = "გთხოვთ გაიაროთ ვერიფიკაცია მითითებულ ელ-ფოსტაზე."
                    }
                } else {
                    _repositoryDialogError.value = "ვწუხვართ, მითითებული სახელი ან პაროლი არასწორია, სცადე განმეორებით."
                }
            }
        }
    }

    fun resetUserPassword(email: String?) {
        if (email!!.isNotEmpty()) {
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { userEmail ->
                    if (userEmail.isSuccessful) {
                        _repositoryDialogError.value = "მიყევით ინსტრუქციას მითითებულ ელ-ფოსტაზე."
                    }
                }
        } else {
            _repositoryDialogError.value = "გთხოვთ მიუთითეთ ელ-ფოსტა."
        }
    }

    fun changeUserFlowValue() {
        _currentUser.value = false
    }

    fun changeRepositoryDialogError() {
        _repositoryDialogError.value = ""
    }
}