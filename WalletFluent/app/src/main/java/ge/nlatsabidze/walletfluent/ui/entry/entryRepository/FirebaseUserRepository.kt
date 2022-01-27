package ge.nlatsabidze.walletfluent.ui.entry.entryRepository

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import ge.nlatsabidze.walletfluent.R
import ge.nlatsabidze.walletfluent.ui.entry.userData.User
import kotlinx.coroutines.flow.MutableStateFlow
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class FirebaseUserRepository @Inject constructor(
    private val application: Application,
    private val firebaseAuth: FirebaseAuth,
    private var database: DatabaseReference
) {

    private var _currentUser = MutableStateFlow<Boolean>(false)
    val currentUser: MutableStateFlow<Boolean> get() = _currentUser

    private var _repositoryDialogError = MutableStateFlow("")
    val repositoryDialog: MutableStateFlow<String> get() = _repositoryDialogError

    fun register(email: String?, password: String?, name: String, balance: Int) {
        if (email!!.isNotEmpty() && password!!.isNotEmpty()) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val user = firebaseAuth.currentUser
                        val uid = user?.uid
                        val userCreationDate = generateCurrentDate()

                        val activeUser = User(email, name, password, balance, userCreationDate)
                        database.child(uid!!).setValue(activeUser).addOnCompleteListener {
                            if (it.isSuccessful) {
                                _currentUser.value = true
                            } else {
                                _repositoryDialogError.value =
                                    application.resources.getString(R.string.NotValidInformation)
                            }
                        }
                    } else {
                        _repositoryDialogError.value =
                            application.resources.getString(R.string.InvalidRegistration)
                    }
                }
        }
    }

    fun login(email: String?, password: String?) {
        if (email!!.isNotEmpty() && password!!.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { Task ->
                if (Task.isSuccessful) {
                    val firebaseUser = firebaseAuth.currentUser
                    if (firebaseUser!!.isEmailVerified) {
                        _currentUser.value = true
                    } else {
                        firebaseUser.sendEmailVerification()
                        _repositoryDialogError.value =
                            application.resources.getString(R.string.VerificationDialog)
                    }
                } else {
                    _repositoryDialogError.value =
                        application.resources.getString(R.string.InvalidRegistration)
                }
            }
        }
    }

    fun resetUserPassword(email: String?) {
        if (email!!.isNotEmpty()) {
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { userEmail ->
                    if (userEmail.isSuccessful) {
                        _repositoryDialogError.value =
                            application.resources.getString(R.string.ResetPasswordDialog)
                    }
                }
        } else {
            _repositoryDialogError.value =
                application.resources.getString(R.string.ResetPasswordUsingEmailDialog)
        }
    }

    fun logOutUser() {
        firebaseAuth.signOut()
    }

    fun changeUserFlowValue() {
        _currentUser.value = false
    }

    fun changeRepositoryDialogError() {
        _repositoryDialogError.value = ""
    }

    private fun generateCurrentDate(): String {
        val currentDateFormatter = SimpleDateFormat("dd/M/yyyy")
        return currentDateFormatter.format(Date())
    }
}