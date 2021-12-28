package ge.nlatsabidze.walletfluent

import android.app.Application
import android.util.Log.d
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


class FirebaseRepository @Inject constructor(private val application: Application) {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private lateinit var database: DatabaseReference

    private var _currentUser = MutableStateFlow<Boolean>(false)
    val currentUser: MutableStateFlow<Boolean> get() = _currentUser

    private var _repositoryDialogError = MutableStateFlow("")
    val repositoryDialog: MutableStateFlow<String> get() = _repositoryDialogError

    fun register(email: String?, password: String?, name: String) {
        if (email!!.isNotEmpty() && password!!.isNotEmpty()) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val user = firebaseAuth.currentUser
                        val uid = user?.uid

                        database = FirebaseDatabase.getInstance("https://walletfluent-b2fe7-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users")
                        _currentUser.value = true
                    } else {
                        _repositoryDialogError.value = "ვწუხვართ, მითითებული ელ-ფოსტა ან პაროლი არავალიდურია."
                    }
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