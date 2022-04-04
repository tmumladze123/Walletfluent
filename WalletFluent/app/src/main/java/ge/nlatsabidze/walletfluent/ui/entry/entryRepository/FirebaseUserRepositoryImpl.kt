package ge.nlatsabidze.walletfluent.ui.entry.entryRepository

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import ge.nlatsabidze.walletfluent.R
import ge.nlatsabidze.walletfluent.extensions.isEmailValid
import ge.nlatsabidze.walletfluent.ui.entry.userData.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class FirebaseUserRepositoryImpl @Inject constructor(
    private val application: Application,
    private val firebaseAuth: FirebaseAuth,
    private var database: DatabaseReference
): FirebaseUserRepository {

    private var _repositoryDialogError = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun register(email: String?, password: String?, name: String, balance: Int): Flow<Boolean> = callbackFlow {
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
                                trySend(true)
                            } else {
                                _repositoryDialogError.value = it.exception?.message.toString()
                            }
                        }
                    } else {
                        _repositoryDialogError.value = task.exception?.message.toString()
                    }
                }
        }

        awaitClose{}
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun login(email: String?, password: String?): Flow<Boolean> = callbackFlow {
        if (email!!.isNotEmpty() && password!!.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { Task ->
                if (Task.isSuccessful) {
                    val firebaseUser = firebaseAuth.currentUser
                    if (firebaseUser!!.isEmailVerified) {
                        trySend(true)
                    } else {
                        firebaseUser.sendEmailVerification()
                        _repositoryDialogError.value =
                            application.resources.getString(R.string.VerificationDialog)
                    }
                } else {
                    _repositoryDialogError.value = Task.exception?.message.toString()
                }
            }

        }
        awaitClose {}
    }

    override fun resetUserPassword(email: String?) {
        if (email!!.isNotEmpty() && email.isEmailValid()) {
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

    fun getDialogErrorValue(): MutableStateFlow<String> {
        return _repositoryDialogError
    }

    fun changeRepositoryDialogError() {
        _repositoryDialogError.value = ""
    }

    fun logOutUser() {
        firebaseAuth.signOut()
    }

    private fun generateCurrentDate(): String {
        val currentDateFormatter = SimpleDateFormat("dd/M/yyyy")
        return currentDateFormatter.format(Date())
    }
}