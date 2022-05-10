package ge.nlatsabidze.walletfluent.ui.entry.entryRepository

import android.app.Application
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import ge.nlatsabidze.walletfluent.R
import ge.nlatsabidze.walletfluent.Resource
import ge.nlatsabidze.walletfluent.extensions.isEmailValid
import ge.nlatsabidze.walletfluent.ui.entry.userData.User
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class FirebaseUserRepositoryImpl @Inject constructor(
    private val application: Application,
    private val firebaseAuth: FirebaseAuth,
    private var database: DatabaseReference,
    private val io: CoroutineDispatcher
) : FirebaseUserRepository {

    private var _repositoryDialogError = MutableStateFlow("")

    override suspend fun register(
        email: String?, password: String?, name: String, balance: Int
    ): Flow<Resource<AuthResult>> = flow {
        try {
            emit(Resource.Loading())
            if (email!!.isNotEmpty() && password!!.isNotEmpty()) {
                val createResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                val user = firebaseAuth.currentUser
                val uid = user?.uid
                val userCreationDate = generateCurrentDate()
                val activeUser = User(email, name, password, balance, userCreationDate)
                database.child(uid!!).setValue(activeUser).await()
                emit(Resource.Success(createResult))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }


    override suspend fun login(email: String?, password: String?): Flow<Resource<AuthResult>> =
        flow {
            try {
                emit(Resource.Loading())
                if (email!!.isNotEmpty() && password!!.isNotEmpty()) {
                    val loginResult =
                        firebaseAuth.signInWithEmailAndPassword(email, password).await()
                    val firebaseUser = firebaseAuth.currentUser
                    if (firebaseUser!!.isEmailVerified) {
                        emit(Resource.Success(loginResult))
                    } else {
                        firebaseUser.sendEmailVerification()
                        emit(Resource.Error(application.resources.getString(R.string.VerificationDialog)))
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }.flowOn(io)

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

    fun logOutUser() = firebaseAuth.signOut()

    private fun generateCurrentDate(): String {
        val currentDateFormatter = SimpleDateFormat("dd/M/yyyy")
        return currentDateFormatter.format(Date())
    }
}