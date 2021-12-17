package ge.nlatsabidze.walletfluent

import android.app.Application
import android.content.DialogInterface
import android.util.Log.d
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class AppRepository @Inject constructor(private val application: Application) {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()


    private var _userMutableLiveFlow = MutableStateFlow(false)
    val userMutableLiveFlow: MutableStateFlow<Boolean> get() = _userMutableLiveFlow
    private var _dialogError = MutableStateFlow(false)
    val dialogError: MutableStateFlow<Boolean> get() = _dialogError

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
                    _dialogError.value = false
                    _userMutableLiveFlow.value = true
                } else {
                    _userMutableLiveFlow.value = false
                    _dialogError.value = true
                }
            }

        }
    }
}