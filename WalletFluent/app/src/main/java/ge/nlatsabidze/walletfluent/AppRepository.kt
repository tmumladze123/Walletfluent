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

    var result: Boolean = false
    private val userMutableLiveFlow = MutableStateFlow(false);
    private val userMutableLiveData: MutableLiveData<FirebaseUser> = MutableLiveData()
    private val dialogVerifyError: MutableLiveData<Boolean> = MutableLiveData()
    private val taskDialogError: MutableLiveData<Boolean> = MutableLiveData()

    fun register(email: String?, password: String?) {
        firebaseAuth.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    userMutableLiveData.postValue(firebaseAuth.currentUser)
                } else {
                    taskDialogError.postValue(true)
                }
            }
    }

     fun login(email: String?, password: String?) {
        d("esaaa :", password.toString() + "  " + email.toString())
        if (email!!.isNotEmpty() && password!!.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                result = it.isSuccessful
            }

        }
    }
        fun getUserMutableLiveData(): MutableLiveData<FirebaseUser> {
            return userMutableLiveData
        }

        fun getTaskDialogError(): MutableLiveData<Boolean> {
            return taskDialogError
        }

        fun getVerifyDialogError(): MutableLiveData<Boolean> {
            return dialogVerifyError
        }
        fun getFlow():   MutableStateFlow <Boolean> {
        return MutableStateFlow(result)
        }

}